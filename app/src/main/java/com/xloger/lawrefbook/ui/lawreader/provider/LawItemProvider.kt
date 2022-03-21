package com.xloger.lawrefbook.ui.lawreader.provider

import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.ui.lawreader.entity.LawItemNode

/**
 * Created on 2022/3/21 22:55.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawItemProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 2
    override val layoutId: Int
        get() = R.layout.item_law_reader_item

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val entity = item as LawItemNode
        helper.setText(R.id.law_item_content, entity.lawItem.content)
    }
}