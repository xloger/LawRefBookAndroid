package com.xloger.lawrefbook.ui.search.entity

import com.chad.library.adapter.base.entity.node.BaseNode

/**
 * Created on 2022/3/20 19:53.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class SearchItemNode(val searchItem: SearchItem) : BaseNode() {
    override val childNode: MutableList<BaseNode>?
        get() = null

}