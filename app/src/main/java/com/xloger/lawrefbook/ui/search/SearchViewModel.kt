package com.xloger.lawrefbook.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.repository.book.BookRepository
import com.xloger.lawrefbook.repository.book.entity.body.Law
import com.xloger.lawrefbook.repository.book.entity.body.forEachItem
import com.xloger.lawrefbook.ui.search.entity.SearchItem
import com.xloger.lawrefbook.ui.search.entity.SearchItemNode
import kotlin.concurrent.thread

class SearchViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {
    private val _searchList = MutableLiveData<Collection<BaseNode>>()
    val searchList: LiveData<Collection<BaseNode>> get() = _searchList

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg

    fun searchSingle(query: String, docId: String) {
        thread {
            val result = bookRepository.getLawByDocId(docId)
            val law = if (result.isSuccess) {
                result.getOrThrow()
            } else {
                _errorMsg.postValue(result.exceptionOrNull()?.message)
                return@thread
            }
            _searchList.postValue(tranSearchLaw(docId, law, query))
        }

    }

    fun searchAll(query: String) {
        thread {
            val list = mutableListOf<BaseNode>()
            bookRepository.getLawRefContainer().groupList.forEach {
                it.docList.forEach { doc ->
                    val law = bookRepository.getSingleLaw(doc)
                    list.addAll(tranSearchLaw(doc.id, law, query))
                }
            }
            _searchList.postValue(list)
        }
    }

    private fun tranSearchLaw(docId: String, law: Law, query: String): List<BaseNode> {
        val list = mutableListOf<BaseNode>()
        law.forEachItem { item, groupList ->
            if (item.content.contains(query)) {
                list.add(SearchItemNode(SearchItem(docId, item, groupList.map { SearchItem.GroupInfo(it.level, it.title) })))
            }
        }
        return list
    }

}