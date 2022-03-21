package com.xloger.lawrefbook.ui.lawreader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.databinding.LawReaderFragmentBinding
import com.xloger.lawrefbook.repository.BookRepository
import com.xloger.lawrefbook.repository.entity.Doc
import com.xloger.lawrefbook.repository.entity.Law
import com.xloger.lawrefbook.ui.lawreader.entity.LawGroupNode

class LawReaderFragment : Fragment() {

    private var _binding: LawReaderFragmentBinding? = null
    private val binding get() = _binding!!

    private val lawReaderAdapter by lazy { LawReaderAdapter() }

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

        val bookRepository = BookRepository(requireContext().assets)
        val testDoc = Doc("刑法", "法律法规/刑法/刑法.md", setOf())
        val law = bookRepository.getSingleLaw(testDoc)
        lawReaderAdapter.setList(tranLaw(law))
    }

    private fun initView() {

        binding.lawRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = lawReaderAdapter
        }
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