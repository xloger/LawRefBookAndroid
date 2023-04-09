package com.xloger.lawrefbook.repository.book.parser

import com.xloger.lawrefbook.repository.book.entity.body.Law

/**
 * Created on 2022/3/21 20:10.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawParser(
    private val lawRegexHelper: LawRegexHelper
) {
    private var hLevel = 0

    private var baseGroup = Law.Group(0, "", mutableListOf(), mutableListOf())

    private var currentContent: StringBuilder = StringBuilder("")

    fun start() {
    }

    fun putLine(content: String) {
        when {
            content.isBlank() -> return
            content.matches("#+ .*".toRegex()) -> {
                checkPutItem()
                hLevel = content.count { it == '#' }
                val hTitle = content.replace("#+ ".toRegex(), "")
                val group = Law.Group(hLevel, hTitle, mutableListOf(), mutableListOf())
                getCurrentGroup(baseGroup, hLevel - 1).groupList.add(group)
            }
            content.matches("第.+条 .*".toRegex()) -> {
                checkPutItem()
                currentContent.append(content).append("\n")
            }
            else -> {
                currentContent.append(content).append("\n")
            }
        }
    }

    /**
     * 如果当前存在可以提交的 Item，则提交
     */
    private fun checkPutItem() {
        if (currentContent.isNotBlank()) {
            val item = lawRegexHelper.parserLawItem(currentContent.toString().removeSuffix("\n"))
            getCurrentGroup(baseGroup, hLevel).itemList.add(item)
            currentContent.clear()
        }
    }

    fun endAndGet() : Law {
        fixOriginText()
        return Law(baseGroup)
    }

    /**
     * 兼容那些没有按照 MD 格式和对应规范发文档。
     */
    private fun fixOriginText() {
        val controlGroup = if (baseGroup.groupList.isEmpty()) baseGroup else baseGroup.groupList.last()
        if (controlGroup.groupList.isEmpty() && controlGroup.itemList.isEmpty() && currentContent.isNotBlank()) {
            currentContent.split("\n").forEach {
                controlGroup.itemList.add(lawRegexHelper.parserLawItem(it))
            }
        }
    }

    fun getCurrentGroup(group: Law.Group, level: Int): Law.Group {
        if (level == 0) return baseGroup

        if (group.level == level) {
            return group
        } else {
            return getCurrentGroup(group.groupList.last(), level)
        }
    }

}