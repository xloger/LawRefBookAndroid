package com.xloger.lawrefbook.repository.book.entity.menu

/**
 * Created on 2022/3/20 15:52.
 * Author: xloger
 * Email:phoenix@xloger.com
 * 一个有完整的目录结构信息的数据模型。里面不包含文件本身的信息。
 */
data class LawRefContainer(val groupList: List<Group>) {


    data class Group(
        val id: String,
        val category: String,
        val folder: String,
        val links: List<String>,
        val docList: List<Doc>
    )
}