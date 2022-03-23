package com.xloger.lawrefbook.ui.lawreader.weight.lawmenu.provider

import android.widget.TextView
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.divider.MaterialDivider
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.ui.lawreader.weight.lawmenu.entity.LawMenuNode
import com.xloger.lawrefbook.util.marginParams
import com.xloger.lawrefbook.util.px
import com.xloger.lawrefbook.util.visibleOrGone

/**
 * Created on 2022/3/20 20:10.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawMenuProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 1
    override val layoutId: Int
        get() = R.layout.item_menu_law_node

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val entity = item as LawMenuNode
        helper.setText(R.id.law_menu_title, entity.group.title)
        val textView = helper.getView<TextView>(R.id.law_menu_title)
        val divider = helper.getView<MaterialDivider>(R.id.law_menu_divider)
        var isShowDivider = true
        when(entity.group.level) {
            1 -> {
                textView.marginParams.marginStart = 8.px
            }
            2 -> {
                textView.marginParams.marginStart = 16.px
            }
            3 -> {
                textView.marginParams.marginStart = 24.px
            }
            4 -> {
                textView.marginParams.marginStart = 32.px
                isShowDivider = false
            }
        }
        divider.visibleOrGone(isShowDivider)
    }

}