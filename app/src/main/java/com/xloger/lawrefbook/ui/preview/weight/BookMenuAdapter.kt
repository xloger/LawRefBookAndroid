package com.xloger.lawrefbook.ui.preview.weight

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.ui.preview.weight.entity.GroupNode
import com.xloger.lawrefbook.ui.preview.weight.entity.ItemNode
import com.xloger.lawrefbook.ui.preview.weight.provider.GroupProvider
import com.xloger.lawrefbook.ui.preview.weight.provider.ItemProvider

/**
 * Created on 2022/3/20 16:23.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class BookMenuAdapter(
    onItemClick: (item: ItemNode) -> Unit
) : BaseNodeAdapter() {

    init {
        addNodeProvider(GroupProvider())
        addNodeProvider(ItemProvider(onItemClick))
    }

    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        return when(data[position]) {
            is GroupNode -> 1
            is ItemNode -> 2
            else -> -1
        }
    }
}