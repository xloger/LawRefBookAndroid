package com.xloger.lawrefbook.ui.lawreader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.repository.book.BookRepository
import com.xloger.lawrefbook.repository.book.entity.body.Law
import com.xloger.lawrefbook.repository.favorites.FavoritesRepository
import com.xloger.lawrefbook.repository.favorites.entity.FavoritesLawItem
import com.xloger.lawrefbook.ui.lawreader.entity.LawGroupNode
import com.xloger.lawrefbook.ui.lawreader.entity.LawItemNode
import com.xloger.lawrefbook.ui.lawreader.weight.lawmenu.entity.LawMenuNode
import kotlin.concurrent.thread

class LawReaderViewModel(
    private val bookRepository: BookRepository,
    private val favRepository: FavoritesRepository
) : ViewModel() {

    private val _law = MutableLiveData<Law>()
    val law: LiveData<Law> get() = _law

    private val _contentList = MutableLiveData<Collection<BaseNode>>()
    val contentList: LiveData<Collection<BaseNode>> get() = _contentList

    private val _menuList = MutableLiveData<Collection<BaseNode>>()
    val menuList: LiveData<Collection<BaseNode>> get() = _menuList

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg

    fun requestLaw(docId: String) {
        bookRepository.getLawByDocId(docId).let {
            if (it.isSuccess) {
                val law1 = it.getOrThrow()
                _law.postValue(law1)
                _contentList.postValue(tranLawContent(law1))
                _menuList.postValue(tranLawMenu(law1))
            } else {
                _errorMsg.postValue(it.exceptionOrNull()?.message)
            }
        }
    }

    private fun tranLawContent(law: Law): List<BaseNode> {
        val list = mutableListOf<BaseNode>()
        list.add(LawGroupNode(law.group))
        return list
    }

    private fun tranLawMenu(law: Law) : List<BaseNode> {
        val list = mutableListOf<BaseNode>()
        law.group.groupList.forEach { group ->
            list.add(LawMenuNode(group))
        }
        return list
    }

    fun favItem(docId: String, lawItemNode: LawItemNode) {
        val lawItem = lawItemNode.lawItem
        thread {
            favRepository.addFavItem(FavoritesLawItem(
                docId = docId,
                content = lawItem.print(),
                timestamp = System.currentTimeMillis()
            ))
        }

    }
}