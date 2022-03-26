package com.xloger.lawrefbook.ui.preview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xloger.lawrefbook.repository.BookRepository
import com.xloger.lawrefbook.repository.entity.LawRefContainer

class PreviewViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _lawRefContainer = MutableLiveData<LawRefContainer>()
    val lawRefContainer: LiveData<LawRefContainer> = _lawRefContainer

    fun requestLawRefContainer() {
        val container = bookRepository.getLawRefContainer()
        _lawRefContainer.postValue(container)
    }
}