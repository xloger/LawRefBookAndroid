package com.xloger.lawrefbook.repository

import com.xloger.lawrefbook.repository.entity.Law
import com.xloger.lawrefbook.util.XLog

/**
 * Created on 2022/3/21 20:10.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawParser {
    private var status: Status = Status.Title
    private var hLevel = 0

    private var title = ""
    private var desc: StringBuilder = StringBuilder("")
    private var baseGroup = Law.Group(1, "", mutableListOf(), mutableListOf())

    private var currentContent: StringBuilder = StringBuilder("")

    fun start() {
        status = Status.Title
        title = ""
        desc = StringBuilder("")

    }

    fun putLine(content: String) {
        when {
            content.isBlank() -> return
//            content.startsWith("# ") -> {
//                title = content.removePrefix("# ")
//                status = Status.Title
//            }
//            (status == Status.Title || status == Status.Desc) && !content.startsWith("#") -> {
//                desc.append(content)
//                desc.append("\n")
//                status = Status.Desc
//            }
//            content == "<!-- INFO END -->" -> {
//                status = Status.Group
//            }
//            content.startsWith("#") -> {
//                XLog.d("startsWith：$content")
//            }
            content.matches("#+ .*".toRegex()) -> {
                XLog.d("符合条件的：$content")
                hLevel = content.count { it == '#' }
                val hTitle = content.replace("#+ ".toRegex(), "")
                val group = Law.Group(hLevel, hTitle, mutableListOf(), mutableListOf())
                getCurrentGroup(baseGroup, hLevel - 1).groupList.add(group)
            }
            content.startsWith("第") -> {
                if (currentContent.isNotBlank()) {
                    val item = Law.Item("", currentContent.toString())
                    getCurrentGroup(baseGroup, hLevel).itemList.add(item)
                    currentContent.clear()
                    currentContent.append(content).append("\n")
                } else {
                    currentContent.append(content).append("\n")
                }
            }
            else -> {
                currentContent.append(content).append("\n")
            }
        }
    }

    fun endAndGet() : Law {
        XLog.d("baseGroup:$baseGroup")
        return Law(title, desc.toString(), baseGroup)
    }

    fun getCurrentGroup(group: Law.Group, level: Int): Law.Group {
        if (level == 0) return group
//        if (group.groupList.isEmpty()) {
//            group.groupList.add(Law.Group(group.level + 1, "", mutableListOf(), mutableListOf()))
//        }
        if (group.level == level) {
            return group
        } else {
            return getCurrentGroup(group.groupList.last(), level)
        }
    }

    enum class Status {
        Title, Desc, Group, Item
    }
}