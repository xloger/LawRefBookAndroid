package com.xloger.lawrefbook.ui.search

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.databinding.SearchFragmentBinding
import com.xloger.lawrefbook.ui.search.entity.SearchItemNode
import com.xloger.lawrefbook.ui.search.entity.SearchUiState
import com.xloger.lawrefbook.util.XLog
import com.xloger.lawrefbook.util.gone
import com.xloger.lawrefbook.util.visible
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private val searchAdapter by lazy { SearchAdapter() }

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initToolBar()
        observe()

        val docId = arguments?.getString("docId")
        val query = arguments?.getString("query") ?: ""
        searchAdapter.setSearchKey(query)
        if (docId != null) {
            viewModel.searchSingle(query, docId)
        } else {
            viewModel.searchAll(query)
        }
    }

    private fun initView() {
        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
        searchAdapter.setEmptyView(R.layout.loading_view)
        searchAdapter.setOnItemClickListener { adapter, _, position ->
            val entity = adapter.data[position]
            when(entity) {
                is SearchItemNode -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("操作")
                        .setItems(listOf<String>(if (entity.isFav) "取消收藏" else "收藏", "复制", "跳转原文").toTypedArray(), object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                val searchItem = entity.searchItem
                                when(p1) {
                                    0 -> {
                                        if (entity.isFav) {
                                            viewModel.cancelFavoriteItem(entity)
                                        } else {
                                            viewModel.favoriteItem(entity)
                                        }
                                    }
                                    1 -> {
                                        fun copy(text: String) {
                                            val clipboard = requireContext().getSystemService(
                                                Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val textCd = ClipData.newPlainText("text", text)
                                            clipboard.setPrimaryClip(textCd)
                                        }
                                        copy(searchItem.lawItem.print())
                                    }
                                    2 -> {
                                        findNavController().navigate(R.id.lawReaderFragment, bundleOf("docId" to searchItem.docId, "jumpText" to searchItem.lawItem.print()))
                                    }
                                }
                            }
                        })
                        .show()
                }
            }
        }
    }

    private fun initToolBar() {
        binding.searchToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.searchToolBar.title = "搜索：${arguments?.getString("query")}"
    }

    private fun observe() {
        lifecycleScope.launchWhenResumed {
            viewModel.uiState.collect {
                XLog.d("uiState: $it")
                when(it) {
                    is SearchUiState.Default -> {
                        binding.searchText.gone()
                    }
                    is SearchUiState.Querying -> {
                        binding.searchText.visible()
                        binding.searchText.text = "正在搜索：${it.queryDoc}"
                    }
                    is SearchUiState.Success -> {
                        binding.searchText.gone()
//                        binding.searchText.text = "搜索结果：${it.list.size}条"
                        binding.searchToolBar.title = "搜索：${arguments?.getString("query")} - ${it.list.size}条"
                        searchAdapter.setList(it.list)
                        if (it.list.isEmpty()) {
                            searchAdapter.setEmptyView(R.layout.empty_view)
                        }
                    }
                    is SearchUiState.Error -> {
                        binding.searchText.gone()
                        Toast.makeText(requireContext(), "${it.msg}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}