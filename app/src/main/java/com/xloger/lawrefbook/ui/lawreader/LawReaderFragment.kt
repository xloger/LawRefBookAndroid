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
import com.xloger.lawrefbook.repository.entity.Law
import com.xloger.lawrefbook.ui.lawreader.entity.LawGroupNode
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


    }

    private fun initView() {
        val bookRepository = BookRepository(requireContext().assets)
        val docPath = arguments?.getString("docPath") ?: "Laws/刑法/刑法.md"
        val law = bookRepository.getSingleLaw(docPath)
        lawReaderAdapter.setList(tranLaw(law))

        binding.lawRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = lawReaderAdapter
        }
        binding.lawMenuFab.apply {
            setOnClickListener {
                lawMenuDialog.apply {
                    create()
                    show()
                    syncContainer(law)
                    listener = object : LawMenuDialog.EventListener {
                        override fun onMenuClick(group: Law.Group) {
//                            Toast.makeText(requireContext(), "${group.title}", Toast.LENGTH_SHORT).show()

                            val index = lawReaderAdapter.data.indexOfFirst { (it as? LawGroupNode)?.group?.title == group.title }
                            XLog.d("index:$index")
                            binding.lawRecyclerView.scrollToPosition(index)
                            dismiss()
                        }
                    }
                }
            }
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