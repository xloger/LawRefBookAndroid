package com.xloger.lawrefbook.ui.preview

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xloger.lawrefbook.MainApplication
import com.xloger.lawrefbook.repository.book.BookRepository
import com.xloger.lawrefbook.repository.book.entity.menu.LawRefContainer
import com.xloger.lawrefbook.util.XLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreviewViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {

    private val _lawRefContainer = MutableLiveData<LawRefContainer>()
    val lawRefContainer: LiveData<LawRefContainer> = _lawRefContainer

    fun requestLawRefContainer() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val container = bookRepository.getLawRefContainer()
//                checkLawRefContainerContentRight(container)
                _lawRefContainer.postValue(container)
            }
        }
    }

    /**
     * 测试容器中的所有文件路径配置正确。
     * 【因为单测的环境配置有点问题，临时在这里写一个测试方法。。。
     */
    private fun checkLawRefContainerPathRight(container: LawRefContainer) {
        val assetManager = MainApplication.koin.get<Context>().assets
        var errorNum = 0
        container.groupList.forEach { group ->
            group.docList.forEach { doc ->
                val path = doc.path
                try {
                    assetManager.open(path).available()
                } catch (e: Exception) {
                    XLog.e("文件路径不存在：${doc}")
                    errorNum++
                }

            }
        }
        XLog.e("错误文件数：$errorNum")
    }

    private fun checkLawRefContainerContentRight(container: LawRefContainer) {
        var errorNum = 0
        container.groupList.forEach { group ->
            group.docList.forEach { doc ->
                try {
                    bookRepository.getSingleLaw(doc).let {
                        if(it.group.groupList.isEmpty() && it.group.itemList.isEmpty()) {
                            XLog.e("文件内容为空：${doc}")
                            errorNum++
                        }
                    }
                } catch (e: Exception) {
                    XLog.e("文件路径不存在：${doc}")
                    errorNum++
                }

            }
        }
        XLog.e("错误文件数：$errorNum")
    }
}