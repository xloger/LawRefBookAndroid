package com.xloger.lawrefbook.ui.search.entity

import com.chad.library.adapter.base.entity.node.BaseNode

/**
 * Created on 2023/4/8 23:37.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
sealed class SearchUiState {
    object Default : SearchUiState()

    data class Querying(val queryDoc: String) : SearchUiState()

    data class Success(val list: Collection<BaseNode>) : SearchUiState()

    data class Error(val throwable: Throwable?, val msg: String? = null) : SearchUiState()
}

