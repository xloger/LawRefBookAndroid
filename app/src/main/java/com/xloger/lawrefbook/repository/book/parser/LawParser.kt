package com.xloger.lawrefbook.repository.book.parser

import com.xloger.lawrefbook.repository.book.entity.body.Law
import com.xloger.lawrefbook.util.XLog
import java.util.regex.Pattern

/**
 * Created on 2022/3/21 20:10.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawParser {
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
            val originText = currentContent.toString().removeSuffix("\n")
            val matcher = Pattern.compile("(第.+?条)( *)([\\s\\S]*)", Pattern.DOTALL).matcher(originText)
            val item = if (matcher.find()) {
                Law.Item(matcher.group(1) ?: "", matcher.group(3) ?: originText)
            } else {
                Law.Item("", originText)
            }
            if (item.print() != originText) {
                XLog.e("解析失败：$item，origin:$originText")
            }
            getCurrentGroup(baseGroup, hLevel).itemList.add(item)
            currentContent.clear()
        }
    }

    fun endAndGet() : Law {
        return Law(baseGroup)
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