package com.xloger.lawrefbook.ui.lawreader.weight.lawmenu

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.xloger.lawrefbook.ui.lawreader.weight.lawmenu.entity.LawMenuNode
import com.xloger.lawrefbook.ui.lawreader.weight.lawmenu.provider.LawMenuProvider

/**
 * Created on 2022/3/20 16:23.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawMenuAdapter(
) : BaseNodeAdapter() {

    init {
        addNodeProvider(LawMenuProvider())
    }

    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        return when(data[position]) {
            is LawMenuNode -> 1
            else -> -1
        }
    }
}