package com.xloger.lawrefbook.ui.lawreader.provider

import android.graphics.Color
import android.util.TypedValue
import android.widget.TextView
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.ui.lawreader.entity.LawGroupNode

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
        helper.setText(R.id.law_group_content, entity.group.title)
        when(entity.group.level) {
            1 -> {
                setStyle(helper, Color.RED, 24f)
            }
            2 -> {
                setStyle(helper, Color.GREEN, 20f)
            }
            3 -> setStyle(helper, Color.BLUE, 16f)
            4 -> setStyle(helper, Color.DKGRAY, 14f)
        }
    }

    private fun setStyle(helper: BaseViewHolder, bgColor: Int, textSizeDp: Float) {
        helper.getView<TextView>(R.id.law_group_content).apply {
            setBackgroundColor(bgColor)
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSizeDp)
        }
    }
}