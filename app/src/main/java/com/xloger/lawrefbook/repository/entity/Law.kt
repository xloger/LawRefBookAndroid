package com.xloger.lawrefbook.repository.entity

/**
 * Created on 2022/3/21 20:12.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class Law(
    val title: String,
    val desc: String,
    val group: Group,
) {

    data class Group(
        val level: Int,
        var title: String,
        val groupList: MutableList<Group>,
        val itemList: MutableList<Item>
    )

    data class Item(
        val article: String,
        val content: String
    )
}