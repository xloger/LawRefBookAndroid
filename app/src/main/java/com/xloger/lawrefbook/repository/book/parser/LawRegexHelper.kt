package com.xloger.lawrefbook.repository.book.parser

import com.xloger.lawrefbook.repository.book.entity.body.Law
import com.xloger.lawrefbook.util.XLog
import java.util.regex.Pattern

/**
 * Created on 2022/3/29 15:11.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawRegexHelper {
    val pattern = Pattern.compile("^(第.+?条)( *)([\\s\\S]*)", Pattern.DOTALL)

    fun parserLawItem(originText: String) : Law.Item {
        val matcher = pattern.matcher(originText)
        val item = if (matcher.find()) {
            Law.Item(matcher.group(1) ?: "", matcher.group(3) ?: originText)
        } else {
            Law.Item("", originText)
        }
        if (item.print() != originText) {
            XLog.e("解析失败：$item，origin:$originText")
        }
        return item
    }
}