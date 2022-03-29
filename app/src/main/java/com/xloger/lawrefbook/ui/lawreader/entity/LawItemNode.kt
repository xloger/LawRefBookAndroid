package com.xloger.lawrefbook.ui.lawreader.entity

import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.repository.book.entity.body.Law

/**
 * Created on 2022/3/20 19:53.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawItemNode(val lawItem: Law.Item) : BaseNode() {
    override val childNode: MutableList<BaseNode>?
        get() = null

}