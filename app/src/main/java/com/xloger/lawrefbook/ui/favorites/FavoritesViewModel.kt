package com.xloger.lawrefbook.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.repository.book.entity.body.Law
import com.xloger.lawrefbook.repository.favorites.FavoritesRepository
import com.xloger.lawrefbook.ui.search.entity.SearchItem
import com.xloger.lawrefbook.ui.search.entity.SearchItemNode
import kotlin.concurrent.thread

class FavoritesViewModel(
    private val favRepository: FavoritesRepository
) : ViewModel() {
    private val _favItemList = MutableLiveData<Collection<BaseNode>>()
    val favItemList: LiveData<Collection<BaseNode>> get() = _favItemList

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg

    fun requestFavItemList() {
        thread {
            val allFavItem = favRepository.getAllFavItem()
            val searchItemNodeList = allFavItem.map { SearchItem(it.docId, Law.Item("", it.content), emptyList() ) }.map { SearchItemNode(it) }
            _favItemList.postValue(searchItemNodeList)
        }
    }


}