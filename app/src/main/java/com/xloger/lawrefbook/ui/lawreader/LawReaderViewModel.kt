package com.xloger.lawrefbook.ui.lawreader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xloger.lawrefbook.repository.book.BookRepository
import com.xloger.lawrefbook.repository.book.entity.body.Law

class LawReaderViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _law = MutableLiveData<Law>()
    val law: LiveData<Law> get() = _law

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg

    fun requestLaw(docId: String) {
        bookRepository.getLawByDocId(docId).let {
            if (it.isSuccess) {
                _law.value = it.getOrThrow()
            } else {
                _errorMsg.value = it.exceptionOrNull()?.message
            }
        }
    }
}