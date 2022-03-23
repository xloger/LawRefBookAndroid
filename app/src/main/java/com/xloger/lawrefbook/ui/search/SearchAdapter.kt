package com.xloger.lawrefbook.ui.search

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.ui.lawreader.entity.LawItemNode
import com.xloger.lawrefbook.ui.lawreader.provider.LawItemProvider

/**
 * Created on 2022/3/21 22:42.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class SearchAdapter(

) : BaseNodeAdapter() {

    init {
        addNodeProvider(LawItemProvider())
    }

    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        return when(data[position]) {
            is LawItemNode -> 2
            else -> -1
        }
    }
}