package com.xloger.lawrefbook.repository.book.entity.body


/**
 * Created on 2022/3/21 20:12.
 * Author: xloger
 * Email:phoenix@xloger.com
 * 一个法律法规文件解析后的数据模型
 */
class Law(
    val group: Group,
) {

    /**
     * 该文件的标题
     */
    val title by lazy {
        group.groupList.firstOrNull()?.title ?: ""
    }

    /**
     * 该文件的描述
     */
    val desc by lazy {
        val firstItem: Item? = group.groupList.firstOrNull()?.itemList?.firstOrNull()
        if (firstItem != null && firstItem.isDesc) {
            firstItem.content.removeSuffix("<!-- INFO END -->")
        } else {
            ""
        }
    }

    /**
     * 一组法律条目，可无限嵌套。
     * @param level 层级。
     * @param title 标题。
     * @param groupList 子组。
     * @param itemList 子条目。
     */
    data class Group(
        val level: Int,
        var title: String,
        val groupList: MutableList<Group>,
        val itemList: MutableList<Item>
    )

    /**
     * 一个法律条目
     * @param article 第几条
     * @param content 内容
     */
    data class Item(
        val article: String,
        val content: String
    ) {
        /**
         * 打印条目
         */
        fun print() = article.let { if (it.isBlank()) "" else "$it " } + content

        /**
         * 是否是描述
         */
        val isDesc by lazy {
            article.isBlank() && content.endsWith("<!-- INFO END -->")
        }
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
    this.group.forEachItem(groupList, action)
}