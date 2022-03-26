package com.xloger.lawrefbook.ui.search

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.ui.search.entity.SearchItemNode
import com.xloger.lawrefbook.ui.search.provider.SearchItemProvider

/**
 * Created on 2022/3/21 22:42.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class SearchAdapter(

) : BaseNodeAdapter() {

    init {
        addNodeProvider(SearchItemProvider())
    }

    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        return when(data[position]) {
            is SearchItemNode -> 1
            else -> -1
        }
    }
}