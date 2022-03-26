package com.xloger.lawrefbook.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.repository.BookRepository
import com.xloger.lawrefbook.repository.entity.Law
import com.xloger.lawrefbook.ui.lawreader.entity.LawItemNode

class SearchViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {
    private val _searchList = MutableLiveData<Collection<BaseNode>>()
    val searchList: LiveData<Collection<BaseNode>> get() = _searchList

    fun searchSingle(query: String, docPath: String) {
        val law = bookRepository.getSingleLaw(docPath)
        _searchList.value = tranSearchLaw(law, query)
    }

    fun searchAll(query: String) {
        val list = mutableListOf<BaseNode>()
        bookRepository.getLawRefContainer().groupList.forEach {
            it.docList.forEach { doc ->
                val law = bookRepository.getSingleLaw(doc)
                list.addAll(tranSearchLaw(law, query))
            }
        }
        _searchList.value = list
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
}