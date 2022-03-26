package com.xloger.lawrefbook.repository.book.entity.body


/**
 * Created on 2022/3/21 20:12.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class Law(
    val group: Group,
) {

    fun title() = group.groupList.first().title

    fun desc() = group.groupList.first().itemList.joinToString("\n") { it.print() }

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

fun Law.Group.forEachItem(action: (Law.Item) -> Unit) {
    this.itemList.forEach {
        action(it)
    }
    this.groupList.forEach {
        it.forEachItem(action)
    }
}

fun Law.Group.forEachItem(groupList: MutableList<Law.Group> = mutableListOf(), action: (Law.Item, groupList: List<Law.Group>) -> Unit) {
    groupList.add(this)
    this.itemList.forEach {
        action(it, groupList)
    }
    this.groupList.forEach {
        it.forEachItem(groupList.toMutableList(), action)
    }
}

fun Law.forEachItem(action: (Law.Item) -> Unit) {
    this.group.forEachItem(action)
}

fun Law.forEachItem(groupList: MutableList<Law.Group> = mutableListOf(), action: (Law.Item, groupList: List<Law.Group>) -> Unit) {
    this.group.forEachItem(mutableListOf(), action)
}