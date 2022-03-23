package com.xloger.lawrefbook.repository.entity


/**
 * Created on 2022/3/21 20:12.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class Law(
    val group: Group,
) {

    fun title() = group.title

    fun desc() = group.itemList.joinToString("\n") { it.print() }

    data class Group(
        val level: Int,
        var title: String,
        val groupList: MutableList<Group>,
        val itemList: MutableList<Item>
    )

    data class Item(
        val article: String,
        val content: String
    ) {
        fun print() = article.let { if (it.isBlank()) "" else "$it " } + content
    }
}