package com.xloger.lawrefbook.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.xloger.lawrefbook.databinding.PreviewFragmentBinding
import com.xloger.lawrefbook.repository.BookRepository
import com.xloger.lawrefbook.repository.entity.Doc
import com.xloger.lawrefbook.ui.preview.weight.BookMenuDialog

class PreviewFragment : Fragment() {

    private var _binding: PreviewFragmentBinding? = null
    private val binding get() = _binding!!

    private val bookMenuDialog: BookMenuDialog by lazy {
        BookMenuDialog(requireContext())
    }

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
        val testDoc = Doc("刑法", "法律法规/刑法/刑法.md", setOf())
        binding.content.text = bookRepository.getSingleDoc(testDoc)
        bookRepository.getSingleLaw(testDoc)

        binding.menuFab.setOnClickListener {
            bookMenuDialog.apply {
                create()
                show()
                listener = object : BookMenuDialog.EventListener {
                    override fun onItemClick(doc: Doc) {
                        binding.content.text = bookRepository.getSingleDoc(doc)
                        bookRepository.getSingleLaw(doc)
                        dismiss()
                    }
                }
                syncContainer(lawRefContainer)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}