package com.xloger.lawrefbook.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.repository.book.BookRepository
import com.xloger.lawrefbook.repository.book.parser.LawRegexHelper
import com.xloger.lawrefbook.repository.favorites.FavoritesRepository
import com.xloger.lawrefbook.repository.favorites.entity.FavoritesLawItem
import com.xloger.lawrefbook.ui.search.entity.SearchItem
import com.xloger.lawrefbook.ui.search.entity.SearchItemNode
import kotlin.concurrent.thread

class FavoritesViewModel(
    private val favRepository: FavoritesRepository,
    private val bookRepository: BookRepository,
    private val lawRegexHelper: LawRegexHelper
) : ViewModel() {
    private val _favItemList = MutableLiveData<Collection<BaseNode>>()
    val favItemList: LiveData<Collection<BaseNode>> get() = _favItemList

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg

    private val favItemNodeList = mutableListOf<SearchItemNode>()

    fun requestFavItemList() {
        thread {
            val allFavItem = favRepository.getAllFavItem()
            val searchItemNodeList = allFavItem.map {
                SearchItem(it.docId, lawRegexHelper.parserLawItem(it.content), emptyList() )
            }.map {
                SearchItemNode(it, docName = bookRepository.getDoc(it.docId)?.name ?: "")
            }
            favItemNodeList.clear()
            favItemNodeList.addAll(searchItemNodeList)
            _favItemList.postValue(favItemNodeList)
        }
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
            favItemNodeList.remove(searchItemNode)
            _favItemList.postValue(favItemNodeList)
        }
    }

}