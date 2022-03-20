package com.xloger.lawrefbook.repository.entity

/**
 * Created on 2022/3/20 15:46.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
data class Doc(
    val name: String,
    val path: String,
    val tags: Collection<Tag>
) {

    data class Tag(
        val name: String
    )
}