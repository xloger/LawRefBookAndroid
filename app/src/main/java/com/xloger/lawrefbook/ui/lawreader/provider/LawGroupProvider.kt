package com.xloger.lawrefbook.ui.lawreader.provider

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.ui.lawreader.entity.LawGroupNode
import com.xloger.lawrefbook.util.gone
import com.xloger.lawrefbook.util.visible

/**
 * Created on 2022/3/21 22:55.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawGroupProvider : BaseNodeProvider() {
    override val itemViewType: Int
        get() = 1
    override val layoutId: Int
        get() = R.layout.item_law_reader_group

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val entity = item as LawGroupNode
        setStyle(helper, entity.group.level, entity.group.title)
    }

    private fun setStyle(helper: BaseViewHolder, level: Int, content: String) {
        val h1Layout = helper.getView<View>(R.id.h1_layout)
        val h1TextView = helper.getView<TextView>(R.id.law_group_content_h1)
        val h2Layout = helper.getView<View>(R.id.h2_layout)
        val h2TextView = helper.getView<TextView>(R.id.law_group_content_h2)
        val h3Layout = helper.getView<View>(R.id.h3_layout)
        val h3TextView = helper.getView<TextView>(R.id.law_group_content_h3)
        val h4Layout = helper.getView<View>(R.id.h4_layout)
        val h4TextView = helper.getView<TextView>(R.id.law_group_content_h4)

        when(level) {
            1 -> h1TextView.text = content
            2 -> h2TextView.text = content
            3 -> h3TextView.text = content
            4 -> h4TextView.text = content
        }
        h1Layout.gone()
        h2Layout.gone()
        h3Layout.gone()
        h4Layout.gone()
        when(level) {
            1 -> h1Layout.visible()
            2 -> h2Layout.visible()
            3 -> h3Layout.visible()
            4 -> h4Layout.visible()
        }

    }
}