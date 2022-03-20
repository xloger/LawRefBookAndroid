package com.xloger.lawrefbook.repository

import android.content.res.AssetManager
import android.util.Log
import com.xloger.lawrefbook.repository.entity.Doc
import com.xloger.lawrefbook.repository.entity.LawRefContainer

/**
 * Created on 2022/3/20 15:31.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class BookRepository(
    val asset: AssetManager
) {

    private val baseDirName = "法律法规"

    fun getSingleDoc(doc: Doc) : String {
        return asset.open(doc.path).bufferedReader().use { it.readText() }
    }

    fun getTypeName() : List<String> {
        return asset.list(baseDirName)?.toList() ?: emptyList()
    }


    fun getLawRefContainer() : LawRefContainer {
        val groupList = mutableListOf<LawRefContainer.Group>()
        asset.list(baseDirName)?.forEach { path ->
            val docList = mutableListOf<Doc>()
            asset.list("$baseDirName/$path")?.forEach { docName ->
                val doc = Doc(docName.removeSuffix(".md"), "$baseDirName/$path/$docName", setOf(Doc.Tag(path)))
                docList.add(doc)
            }
            groupList.add(LawRefContainer.Group(Doc.Tag(path), docList))
        }

        return LawRefContainer(groupList)
    }

}