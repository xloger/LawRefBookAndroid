package com.xloger.lawrefbook.repository.entity

/**
 * Created on 2022/3/20 15:52.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
data class LawRefContainer(val groupList: List<Group>) {


    data class Group(
        val category: String,
        val folder: String,
        val links: List<String>,
        val docList: List<Doc>
    )
}