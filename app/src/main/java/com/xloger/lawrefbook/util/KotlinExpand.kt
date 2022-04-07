package com.xloger.lawrefbook.util

import android.content.Context
import android.content.res.Resources
import android.text.Spannable
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

/**
 * Created on 2022/3/22 22:01.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val View.marginParams: ViewGroup.MarginLayoutParams
    get() = (this.layoutParams as ViewGroup.MarginLayoutParams)

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visibleOrGone(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun View.visibleOrInvisible(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

@ColorInt
fun Context.themeColor(@AttrRes attrRes: Int): Int = TypedValue()
    .apply { theme.resolveAttribute (attrRes, this, true) }
    .data


fun Spannable.customStyle(spannable: Spannable, content: String, start: Int, styleText: String, styleBlock: () -> Any) {
    if (content.isBlank()) return
    val startPos: Int = start + content.substring(start).indexOf(styleText)
    if (startPos > -1) {
        val endPos: Int = startPos + styleText.length
        if (content.substring(endPos).contains(styleText)) {
            customStyle(this, content, endPos, styleText, styleBlock)
        }
        spannable.setSpan(styleBlock(), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}