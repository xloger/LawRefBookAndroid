package com.xloger.lawrefbook.ui.search.provider

import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.ui.search.entity.SearchItemNode

/**
 * Created on 2022/3/26 23:35.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class SearchItemProvider: BaseNodeProvider() {
    override val itemViewType: Int
        get() = 1
    override val layoutId: Int
        get() = R.layout.item_search_item

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val entity = item as SearchItemNode
        helper.setText(R.id.search_item_content, entity.searchItem.lawItem.content)
        val menuPrint = entity.searchItem.groupInfoList
            .map { it.title }
            .filter { it.isNotBlank() }
            .joinToString(">")
        helper.setText(R.id.search_item_menu, "章节：" + menuPrint)
    }
}