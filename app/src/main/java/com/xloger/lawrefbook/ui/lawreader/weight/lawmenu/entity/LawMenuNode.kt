package com.xloger.lawrefbook.ui.lawreader.weight.lawmenu.entity

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.repository.book.entity.body.Law

/**
 * Created on 2022/3/20 19:51.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawMenuNode(
    val group: Law.Group
) : BaseExpandNode() {
    override val childNode: MutableList<BaseNode>?
        get() = group.groupList.map { LawMenuNode(it) }.toMutableList()

}