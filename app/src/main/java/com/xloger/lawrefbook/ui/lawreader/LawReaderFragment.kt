package com.xloger.lawrefbook.ui.lawreader

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.entity.node.BaseNode
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.databinding.LawReaderFragmentBinding
import com.xloger.lawrefbook.repository.BookRepository
import com.xloger.lawrefbook.repository.entity.Law
import com.xloger.lawrefbook.ui.lawreader.entity.LawGroupNode
import com.xloger.lawrefbook.ui.lawreader.entity.LawItemNode
import com.xloger.lawrefbook.ui.lawreader.weight.lawmenu.LawMenuDialog
import com.xloger.lawrefbook.util.XLog

class LawReaderFragment : Fragment() {

    private var _binding: LawReaderFragmentBinding? = null
    private val binding get() = _binding!!

    private val lawReaderAdapter by lazy { LawReaderAdapter() }
    private val lawMenuDialog by lazy { LawMenuDialog(requireContext()) }

    private lateinit var viewModel: LawReaderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LawReaderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(LawReaderViewModel::class.java)
        initView()
        initToolBar()

    }

    private fun initView() {
        val bookRepository = BookRepository(requireContext().assets)
        val docPath = arguments?.getString("docPath") ?: "Laws/刑法/刑法.md"
        val law = bookRepository.getSingleLaw(docPath)
        lawReaderAdapter.setList(tranLaw(law))
        binding.lawReaderToolBar.title = law.title()

        binding.lawRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = lawReaderAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        binding.lawMenuFab.hide()
                    } else if (dy < 0) {
                        binding.lawMenuFab.show()
                    }
                }

            })
        }
        lawMenuDialog.apply {
            listener = object : LawMenuDialog.EventListener {
                override fun onMenuClick(group: Law.Group) {
                    val index = lawReaderAdapter.data.indexOfFirst { (it as? LawGroupNode)?.group?.title == group.title }
                    XLog.d("index:$index")
                    binding.lawRecyclerView.scrollToPosition(index)
                    dismiss()
                }
            }
            create()
            syncContainer(law)
        }
        binding.lawMenuFab.apply {
            setOnClickListener {
                lawMenuDialog.show()
            }
        }
        lawReaderAdapter.setOnItemClickListener { adapter, _, position ->
            val entity = adapter.data[position]
            when(entity) {
                is LawGroupNode -> {

                }
                is LawItemNode -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("操作")
                        .setItems(listOf<String>("喜欢", "复制").toTypedArray(), object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                when(p1) {
                                    0 -> {
                                        Toast.makeText(requireContext(), "还未支持", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    1 -> {
                                        fun copy(text: String) {
                                            val clipboard = requireContext().getSystemService(
                                                Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val textCd = ClipData.newPlainText("text", text)
                                            clipboard.setPrimaryClip(textCd)
                                        }
                                        copy(entity.lawItem.print())
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
        binding.lawReaderToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.lawReaderToolBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.app_bar_menu -> {
                    lawMenuDialog.show()
                    true
                }
                else -> false
            }
        }
        val findItem = binding.lawReaderToolBar.menu.findItem(R.id.app_bar_search)
        val searchView = findItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                if (!searchView.isIconified) {
                    searchView.isIconified = true
                }
                findItem.collapseActionView()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun search(query: String) {
        findNavController().navigate(R.id.searchFragment, bundleOf("query" to query, "docPath" to (arguments?.getString("docPath") ?: "Laws/刑法/刑法.md")))
    }

    private fun tranLaw(law: Law): List<BaseNode> {
        val list = mutableListOf<BaseNode>()
        list.add(LawGroupNode(law.group))
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}