package com.xloger.lawrefbook.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.databinding.PreviewFragmentBinding
import com.xloger.lawrefbook.repository.book.entity.menu.Doc
import com.xloger.lawrefbook.repository.book.entity.menu.LawRefContainer
import com.xloger.lawrefbook.ui.preview.weight.BookMenuAdapter
import com.xloger.lawrefbook.ui.preview.weight.entity.GroupNode
import com.xloger.lawrefbook.ui.preview.weight.entity.ItemNode
import org.koin.androidx.viewmodel.ext.android.viewModel

class PreviewFragment : Fragment() {

    private var _binding: PreviewFragmentBinding? = null
    private val binding get() = _binding!!

    private val menuAdapter by lazy { BookMenuAdapter() }

    private val viewModel: PreviewViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PreviewFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initToolBar()
        observe()

        viewModel.requestLawRefContainer()
    }


    private fun initView() {

        binding.menuRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menuAdapter
        }
        menuAdapter.listener = object : BookMenuAdapter.EventListener {
            override fun onItemClick(doc: Doc) {
                findNavController().navigate(R.id.lawReaderFragment, bundleOf("docId" to doc.id))
            }
        }
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = resources.getString(R.string.app_name)
        }
    }

    private fun initToolBar() {
        binding.previewToolBar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.app_bar_fav -> {
                    findNavController().navigate(R.id.favoritesFragment)
                    true
                }
                R.id.app_bar_about -> {
                    findNavController().navigate(R.id.aboutFragment)
                    true
                }
                else -> false
            }
        }
        val findItem = binding.previewToolBar.menu.findItem(R.id.app_bar_search)
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

    private fun observe() {
        viewModel.lawRefContainer.observe(viewLifecycleOwner) {
            if (menuAdapter.data.isNotEmpty()) return@observe
            menuAdapter.setList(tranContainer(it))
        }
    }

    private fun search(query: String) {
        findNavController().navigate(R.id.searchFragment, bundleOf("query" to query))
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