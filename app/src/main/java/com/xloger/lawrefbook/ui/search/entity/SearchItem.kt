package com.xloger.lawrefbook.ui.search.entity

import com.xloger.lawrefbook.repository.book.entity.body.Law

/**
 * Created on 2022/3/26 23:07.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class SearchItem(
    val docId: String,
    val lawItem: Law.Item,
    val groupInfoList: List<GroupInfo>
) {

    data class GroupInfo(
        val level: Int,
        val title: String
    )
}