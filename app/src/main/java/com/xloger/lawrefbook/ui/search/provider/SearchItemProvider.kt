package com.xloger.lawrefbook.ui.search.provider

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xloger.lawrefbook.R
import com.xloger.lawrefbook.ui.search.entity.SearchItemNode
import com.xloger.lawrefbook.util.gone
import com.xloger.lawrefbook.util.visible


/**
 * Created on 2022/3/26 23:35.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class SearchItemProvider: BaseNodeProvider() {

    var searchKey: String = ""

    override val itemViewType: Int
        get() = 1
    override val layoutId: Int
        get() = R.layout.item_search_item

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        val entity = item as SearchItemNode
        val content = entity.searchItem.lawItem.content
//        val spannedString = buildSpannedString {
//            setSpan()
//        }
        val spannable: Spannable = SpannableString(content)
        highLightText(spannable, content, 0)
        helper.setText(R.id.search_item_content, spannable)
        val menuPrint = entity.searchItem.groupInfoList
            .map { it.title }
            .filter { it.isNotBlank() }
            .joinToString(" > ")
        val menuTextView = helper.getView<TextView>(R.id.search_item_menu)
        if (menuPrint.isNotBlank()) {
            menuTextView.visible()
            menuTextView.text = "目录：$menuPrint"
        } else if (entity.docName.isNotBlank()) {
            menuTextView.visible()
            menuTextView.text = entity.docName
        } else {
            menuTextView.gone()
        }
    }

    /**
     * code from:<https://stackoverflow.com/a/67464940>
     */
    private fun highLightText(spannable: Spannable, string: String, start: Int) {
        if (searchKey.isBlank()) return
        val filterPattern = searchKey
        val startPos: Int = start + string.substring(start)
            .indexOf(filterPattern)
        if (startPos > -1) {
            val endPos: Int = startPos + filterPattern.length
            if (string.substring(endPos)
                    .contains(filterPattern)
            ) {
                highLightText(spannable, string, endPos)
            }
            val blueColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(context, R.color.search_key)))
            val highlightSpan = TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null)
            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}