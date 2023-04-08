package com.xloger.lawrefbook.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.repository.book.BookRepository
import com.xloger.lawrefbook.repository.book.entity.body.Law
import com.xloger.lawrefbook.repository.book.entity.body.forEachItem
import com.xloger.lawrefbook.repository.favorites.FavoritesRepository
import com.xloger.lawrefbook.repository.favorites.entity.FavoritesLawItem
import com.xloger.lawrefbook.ui.search.entity.SearchItem
import com.xloger.lawrefbook.ui.search.entity.SearchItemNode
import com.xloger.lawrefbook.ui.search.entity.SearchUiState
import com.xloger.lawrefbook.util.XLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class SearchViewModel(
    private val bookRepository: BookRepository,
    private val favRepository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Default)
    val uiState get() = _uiState.asStateFlow()

    fun searchSingle(query: String, docId: String) {
        viewModelScope.launch(context = Dispatchers.IO) {
            _uiState.emit(SearchUiState.Querying(docId))
            val result = bookRepository.getLawByDocId(docId)
            val law = result.getOrElse {
                _uiState.emit(SearchUiState.Error(it))
                return@launch
            }
            _uiState.emit(SearchUiState.Success(tranSearchLaw(docId, law, query)))
        }
    }

    fun searchAll(query: String) {
        viewModelScope.launch(context = Dispatchers.IO) {
            val list = mutableListOf<BaseNode>()
            bookRepository.getLawRefContainer().groupList.forEach {
                it.docList.forEach { doc ->
                    _uiState.emit(SearchUiState.Querying(doc.name))
                    val law = bookRepository.getLawByDocId(doc.id).getOrElse {
                        XLog.e("搜索异常：$it")
                        return@forEach
                    }
                    list.addAll(tranSearchLaw(doc.id, law, query))
                }
            }
            _uiState.emit(SearchUiState.Success(list))
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


    fun favoriteItem(searchItemNode: SearchItemNode) {
        thread {
            favRepository.addFavItem(
                FavoritesLawItem(
                docId = searchItemNode.searchItem.docId,
                content = searchItemNode.searchItem.lawItem.print(),
                timestamp = System.currentTimeMillis()
            )
            )
            searchItemNode.isFav = true
        }

    }

    fun cancelFavoriteItem(searchItemNode: SearchItemNode) {
        thread {
            favRepository.removeFavItem(searchItemNode.searchItem.docId, searchItemNode.searchItem.lawItem.print())
            searchItemNode.isFav = false
        }
    }

}