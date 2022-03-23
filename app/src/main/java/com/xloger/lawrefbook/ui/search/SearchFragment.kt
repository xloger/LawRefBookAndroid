package com.xloger.lawrefbook.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.databinding.SearchFragmentBinding
import com.xloger.lawrefbook.repository.BookRepository
import com.xloger.lawrefbook.repository.entity.Law
import com.xloger.lawrefbook.ui.lawreader.entity.LawItemNode

class SearchFragment : Fragment() {

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private val searchAdapter by lazy { SearchAdapter() }

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        initView()
        initToolBar()

        val bookRepository = BookRepository(requireContext().assets)
        val docPath = arguments?.getString("docPath") ?: "Laws/刑法/刑法.md"
        val query = arguments?.getString("query") ?: ""
        val law = bookRepository.getSingleLaw(docPath)
        searchAdapter.setList(tranSearchLaw(law, query))
    }

    private fun initView() {
        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    private fun initToolBar() {
        binding.searchToolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.searchToolBar.title = "搜索：${arguments?.getString("query")}"
    }

    private fun tranSearchLaw(law: Law, query: String): List<BaseNode> {
        val list = mutableListOf<BaseNode>()
        eachItem(law.group).forEach {
            if (it.content.contains(query)) {
                list.add(LawItemNode(it))
            }
        }
        return list
    }

    private fun eachItem(group: Law.Group) : List<Law.Item> {
        val list = mutableListOf<Law.Item>()
        list.addAll(group.itemList)
        group.groupList.forEach {
            list.addAll(eachItem(it))
        }
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}