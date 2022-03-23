package com.xloger.lawrefbook.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.databinding.PreviewFragmentBinding
import com.xloger.lawrefbook.repository.BookRepository
import com.xloger.lawrefbook.repository.entity.Doc
import com.xloger.lawrefbook.repository.entity.LawRefContainer
import com.xloger.lawrefbook.ui.preview.weight.BookMenuAdapter
import com.xloger.lawrefbook.ui.preview.weight.entity.GroupNode
import com.xloger.lawrefbook.ui.preview.weight.entity.ItemNode

class PreviewFragment : Fragment() {

    private var _binding: PreviewFragmentBinding? = null
    private val binding get() = _binding!!


    private val menuAdapter by lazy { BookMenuAdapter() }


    private lateinit var viewModel: PreviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PreviewFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PreviewViewModel::class.java)
        initView()
    }


    private fun initView() {
        val bookRepository = BookRepository(requireContext().assets)
        val lawRefContainer = bookRepository.getLawRefContainer()

        binding.menuRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menuAdapter
        }
        menuAdapter.setList(tranContainer(lawRefContainer))
        menuAdapter.listener = object : BookMenuAdapter.EventListener {
            override fun onItemClick(doc: Doc) {
                findNavController().navigate(R.id.lawReaderFragment, bundleOf("docPath" to doc.path))
            }
        }
    }

    private fun tranContainer(lawRefContainer: LawRefContainer) : List<BaseNode> {
        val list = mutableListOf<BaseNode>()
        lawRefContainer.groupList.forEach { group ->
            list.add(GroupNode(group, group.docList.map { ItemNode(it) }))
        }
        return list
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}