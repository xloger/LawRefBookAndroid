package com.xloger.lawrefbook.ui.lawreader

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xloger.lawrefbook.repository.BookRepository
import com.xloger.lawrefbook.repository.entity.Law

class LawReaderViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _law = MutableLiveData<Law>()
    val law: LiveData<Law> get() = _law


    fun requestLaw(path: String) {
        val singleLaw = bookRepository.getSingleLaw(path)
        _law.value = singleLaw
    }
}