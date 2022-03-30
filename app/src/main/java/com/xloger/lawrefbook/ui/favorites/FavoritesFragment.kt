package com.xloger.lawrefbook.ui.favorites

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.databinding.FavoritesFragmentBinding
import com.xloger.lawrefbook.ui.search.SearchAdapter
import com.xloger.lawrefbook.ui.search.entity.SearchItemNode
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!

    private val favAdapter by lazy { SearchAdapter() }


    private val viewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavoritesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initToolBar()
        observe()

        viewModel.requestFavItemList()
    }

    private fun initView() {
        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favAdapter
        }
        favAdapter.setEmptyView(R.layout.empty_view)
        favAdapter.setOnItemClickListener { adapter, _, position ->
            val entity = adapter.data[position]
            when(entity) {
                is SearchItemNode -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("操作")
                        .setItems(listOf<String>("取消收藏", "复制", "跳转原文").toTypedArray(), object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                val searchItem = entity.searchItem
                                when(p1) {
                                    0 -> {
                                        viewModel.cancelFavoriteItem(entity)
                                        adapter.notifyItemRemoved(position)
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
        binding.favoritesToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observe() {
        viewModel.favItemList.observe(viewLifecycleOwner) {
            favAdapter.setList(it)
        }
        viewModel.errorMsg.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}