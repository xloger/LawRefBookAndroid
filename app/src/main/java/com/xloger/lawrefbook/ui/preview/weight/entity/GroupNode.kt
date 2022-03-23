package com.xloger.lawrefbook.ui.preview.weight.entity

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.repository.entity.LawRefContainer

/**
 * Created on 2022/3/20 19:51.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class GroupNode(
    val group: LawRefContainer.Group,
    val itemList: List<ItemNode>
) : BaseExpandNode() {
    override val childNode: MutableList<BaseNode>?
        get() = itemList.toMutableList()

    fun name() = group.category
}