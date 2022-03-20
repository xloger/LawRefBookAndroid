package com.xloger.lawrefbook.ui.preview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xloger.lawrefbook.databinding.PreviewFragmentBinding
import com.xloger.lawrefbook.repository.BookRepository
import com.xloger.lawrefbook.repository.entity.Doc
import com.xloger.lawrefbook.ui.preview.weight.BookMenuDialog

class PreviewFragment : Fragment() {

    companion object {
        fun newInstance() = PreviewFragment()
    }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PreviewViewModel::class.java)
        initView()
    }

    private fun initView() {
        val bookRepository = BookRepository(requireContext().assets)
        val lawRefContainer = bookRepository.getLawRefContainer()
        binding.menuFab.setOnClickListener {
            bookMenuDialog.apply {
                create()
                show()
                listener = object : BookMenuDialog.EventListener {
                    override fun onItemClick(doc: Doc) {
                        binding.content.text = bookRepository.getSingleDoc(doc)
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