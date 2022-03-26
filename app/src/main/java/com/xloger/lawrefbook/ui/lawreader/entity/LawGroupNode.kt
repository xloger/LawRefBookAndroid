package com.xloger.lawrefbook.ui.lawreader.entity

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.repository.book.entity.body.Law

/**
 * Created on 2022/3/20 19:51.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawGroupNode(
    val group: Law.Group
) : BaseExpandNode() {
    override val childNode: MutableList<BaseNode>?
//        get() = if (group.groupList.isNotEmpty()) {
//            group.groupList.map { LawGroupNode(it) }
//        } else {
//            group.itemList.map { LawItemNode(it) }
//        }.toMutableList()
    get() = (group.itemList.map { LawItemNode(it) } + group.groupList.map { LawGroupNode(it) }).toMutableList()


}