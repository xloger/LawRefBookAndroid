package com.xloger.lawrefbook.ui.preview.weight.provider

import android.view.View
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.ui.preview.weight.entity.GroupNode
import com.xloger.lawrefbook.ui.preview.weight.entity.ItemNode

/**
 * Created on 2022/3/20 20:10.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class ItemProvider(
    val onItemClick: (item: ItemNode) -> Unit
) : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 2
    override val layoutId: Int
        get() = R.layout.item_menu_book_node_item

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val entity = item as ItemNode
        helper.setText(R.id.item_name, entity.name())
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        onItemClick(data as ItemNode)
    }
}