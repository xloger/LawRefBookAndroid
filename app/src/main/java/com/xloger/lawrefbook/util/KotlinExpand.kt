package com.xloger.lawrefbook.util

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup

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