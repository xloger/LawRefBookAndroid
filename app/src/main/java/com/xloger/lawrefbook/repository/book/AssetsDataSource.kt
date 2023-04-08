package com.xloger.lawrefbook.repository.book

import android.content.Context
import com.xloger.lawrefbook.repository.book.entity.body.Law
import com.xloger.lawrefbook.repository.book.entity.menu.Doc
import com.xloger.lawrefbook.repository.book.entity.menu.LawDataDb
import com.xloger.lawrefbook.repository.book.entity.menu.LawRefContainer
import com.xloger.lawrefbook.repository.book.parser.LawParser
import com.xloger.lawrefbook.repository.book.parser.LawRegexHelper
import com.xloger.lawrefbook.repository.book.sqlite.LawDbHelper
import com.xloger.lawrefbook.util.XLog

/**
 * Created on 2022/3/26 20:57.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class AssetsDataSource(
    val context: Context,
    private val lawRegexHelper: LawRegexHelper
) : BookDataSource {
    private val asset = context.assets
    private val baseDirName = "Laws"

    override fun getLawRefContainer(): LawRefContainer {
        return parseDbBySqlite()
    }

    private fun parseDbBySqlite(): LawRefContainer {
        val groupList = mutableListOf<LawRefContainer.Group>()
        val dbHelper = LawDbHelper(context)
        val categoryList = dbHelper.getCategory()
        val lawList = dbHelper.getLaw()
        //将所有法律文件按 categoryId 分组，再按组添加。
        lawList.groupBy { it.categoryId }.map { (categoryId, lawList) ->
            val group = categoryList.first { it.id == categoryId }
            val docList = lawList.map { lawTran(group.folder, it) }.sortedBy { it.order }
            LawRefContainer.Group(
                id = group.id.toString(),
                category = group.name,
                folder = group.folder,
                docList = docList,
                order = group.order
            )
        }.sortedBy { it.order }.run {
            groupList.addAll(this)
        }

        return LawRefContainer(groupList)
    }

    /**
     * 将数据库中的数据转换为 Doc
     * @param folder 所在的父目录
     * @param lawData 数据库中的数据
     */
    private fun lawTran(folder: String, lawData: LawDataDb.Law) : Doc {
        return lawData.run {
            val path = when {
                subTitle != null -> "$baseDirName/${folder}/${subTitle}.md"
                fileName != null -> "$baseDirName/${folder}/${fileName}.md"
                publish != null -> "$baseDirName/${folder}/${name}($publish).md"
                else -> "$baseDirName/${folder}/${name}.md"
            }
            Doc(
                name = name,
                fileName = fileName,
                id = id,
                level = level,
                path = path,
                order = order,
                tags = emptyList()
            )
        }
    }

    /**
     * 根据目录信息得到真实文件内容
     */
    override fun getLaw(doc: Doc): Law {
        val parser = LawParser(lawRegexHelper)
        parser.start()
        try {
            asset.open(doc.path).bufferedReader().forEachLine {
                parser.putLine(it)
            }
        } catch (ex: Exception) {
            XLog.e(ex)
        }

        return parser.endAndGet()
    }

    /**
     * 根据目录信息得到其原始文本
     */
    override fun getOriginText(doc: Doc): String {
        return asset.open(doc.path).bufferedReader().use { it.readText() }
    }

}