package com.xloger.lawrefbook.repository.book

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.xloger.lawrefbook.repository.book.database.LawDatabase
import com.xloger.lawrefbook.repository.book.entity.body.Law
import com.xloger.lawrefbook.repository.book.entity.menu.*
import com.xloger.lawrefbook.repository.book.parser.LawParser
import com.xloger.lawrefbook.repository.book.parser.LawRegexHelper
import com.xloger.lawrefbook.repository.book.sqlite.LawDbHelper
import com.xloger.lawrefbook.util.XLog
import kotlin.concurrent.thread

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

    private fun parseJson(): LawRefContainer {
        val groupList = mutableListOf<LawRefContainer.Group>()
        val dataJsonText = asset.open("$baseDirName/data.json").bufferedReader().readText()
        val lawData = Gson().fromJson(dataJsonText, LawData::class.java)
        lawData.forEach { folderItem ->
            val docList = mutableListOf<Doc>()
            folderItem.laws.forEach { lawItem ->
                val doc = Doc(
                    name = lawItem.name,
                    fileName = lawItem.filename ?: lawItem.name,
                    id = lawItem.id,
                    level = lawItem.level,
                    links = lawItem.links ?: emptyList(),
                    path = "$baseDirName/${folderItem.folder}/${lawItem.filename ?: lawItem.name}.md",
                    tags = emptyList()
                )
                docList.add(doc)
            }

            groupList.add(
                LawRefContainer.Group(
                    id = folderItem.id,
                    category = folderItem.category,
                    folder = folderItem.folder,
                    links = folderItem.links ?: emptyList(),
                    docList = docList
                ))
        }

        return LawRefContainer(groupList)
    }

    /**
     * 该方法因为 ROOM 无法映射 DATE 类型，废弃
     */
    @Deprecated("该方法因为 ROOM 无法映射 DATE 类型，废弃")
    private fun parseDB(): LawRefContainer {
        val groupList = mutableListOf<LawRefContainer.Group>()
        val lawDatabase = Room.databaseBuilder(context, LawDatabase::class.java, "Sample.db")
            .createFromAsset("Laws/db.sqlite3")
            .addMigrations(LawDataMjigration())
            .fallbackToDestructiveMigration()
            .build()
        thread {
            val category = lawDatabase.lawDao().getCategory()
            XLog.d(category.toString())
        }


        return LawRefContainer(groupList)
    }

    private fun parseDbBySqlite(): LawRefContainer {
        val groupList = mutableListOf<LawRefContainer.Group>()
        val dbHelper = LawDbHelper(context)
        val categoryList = dbHelper.getCategory()
        XLog.d(categoryList.toString())
        val lawList = dbHelper.getLaw()
        lawList.forEach {
            XLog.d(it.toString())
        }
        lawList.groupBy { it.categoryId }.map { (categoryId, lawList) ->
            val group = categoryList.first { it.id == categoryId }
            val docList = lawList.map { lawTran(group.name, it) }
            LawRefContainer.Group(
                id = group.id.toString(),
                category = group.name,
                folder = group.folder,
                links = emptyList(),
                docList = docList
            )
        }.run {
            groupList.addAll(this)
        }

        return LawRefContainer(groupList)
    }

    private fun lawTran(folder: String, lawData: LawDataDb.Law) : Doc {
        return lawData.run {
            val path = when {
                publish != null -> "$baseDirName/${folder}/${fileName ?: name}($publish).md"
                else -> "$baseDirName/${folder}/${fileName ?: name}.md"
            }
            Doc(
                name = name,
                fileName = fileName ?: name,
                id = id,
                level = level,
                path = path,
                links = emptyList(),
                tags = emptyList()
            )
        }
    }

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

    override fun getOriginText(doc: Doc): String {
        return asset.open(doc.path).bufferedReader().use { it.readText() }
    }

}