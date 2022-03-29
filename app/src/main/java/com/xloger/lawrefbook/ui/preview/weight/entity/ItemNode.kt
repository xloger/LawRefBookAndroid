package com.xloger.lawrefbook.ui.preview.weight.entity

import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.repository.book.entity.menu.Doc

/**
 * Created on 2022/3/20 19:53.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class ItemNode(val doc: Doc) : BaseNode() {
    override val childNode: MutableList<BaseNode>?
        get() = null

    fun name() = doc.name
}