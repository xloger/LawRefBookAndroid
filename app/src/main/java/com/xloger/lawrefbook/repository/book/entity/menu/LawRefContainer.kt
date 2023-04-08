package com.xloger.lawrefbook.repository.book.entity.menu

/**
 * Created on 2022/3/20 15:52.
 * Author: xloger
 * Email:phoenix@xloger.com
 * 一个有完整的目录结构信息的数据模型。里面不包含文件本身的信息。
 */
data class LawRefContainer(val groupList: List<Group>) {

    /**
     * 一组目录信息
     * @param id 该组目录的id
     * @param category 该组目录所属的分类
     * @param folder 该目录的文件夹路径
     * @param docList 包含的法律法规文件列表
     */
    data class Group(
        val id: String,
        val category: String,
        val folder: String,
        val docList: List<Doc>
    ) {
        override fun toString(): String {
            return "Group(id='$id', category='$category', folder='$folder', docList=${docList.map { it.name }})"
        }
    }

    override fun toString(): String {
        return "LawRefContainer(groupList=${groupList.joinToString("\n")})"
    }


}