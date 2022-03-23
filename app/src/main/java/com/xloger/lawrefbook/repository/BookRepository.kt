package com.xloger.lawrefbook.repository

import android.content.res.AssetManager
import com.google.gson.Gson
import com.xloger.lawrefbook.repository.entity.Doc
import com.xloger.lawrefbook.repository.entity.Law
import com.xloger.lawrefbook.repository.entity.LawData
import com.xloger.lawrefbook.repository.entity.LawRefContainer

/**
 * Created on 2022/3/20 15:31.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class BookRepository(
    val asset: AssetManager
) {

    private val baseDirName = "Laws"

    fun getSingleDoc(doc: Doc) : String {
        return asset.open(doc.path).bufferedReader().use { it.readText() }
    }

    fun getSingleLaw(doc: Doc): Law {
        val parser = LawParser()
        parser.start()
        asset.open(doc.path).bufferedReader().forEachLine {
            parser.putLine(it)
        }
        return parser.endAndGet()
    }

    fun getSingleLaw(path: String): Law {
        val parser = LawParser()
        parser.start()
        asset.open(path).bufferedReader().forEachLine {
            parser.putLine(it)
        }
        return parser.endAndGet()
    }


    fun getLawRefContainer() : LawRefContainer {
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

            groupList.add(LawRefContainer.Group(
                category = folderItem.category,
                folder = folderItem.folder,
                links = folderItem.links ?: emptyList(),
                docList = docList
            ))
        }

        return LawRefContainer(groupList)
    }

}