package com.xloger.lawrefbook.repository.book.entity.menu

/**
 * Created on 2022/3/20 15:46.
 * Author: xloger
 * Email:phoenix@xloger.com
 * 一个法律法规的文件信息（不包含具体的内容）
 * @param name 文件名
 * @param fileName 文件名（带后缀）
 */
data class Doc(
    val name: String,
    val fileName: String,
    val id: String,
    val level: String,
    val path: String,
    val links: List<String>,
    val tags: Collection<Tag>
) {

    data class Tag(
        val name: String
    )
}