package com.xloger.lawrefbook.repository.book

import com.xloger.lawrefbook.repository.book.entity.body.Law
import com.xloger.lawrefbook.repository.book.entity.menu.Doc
import com.xloger.lawrefbook.repository.book.entity.menu.LawRefContainer

/**
 * Created on 2022/3/20 15:31.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class BookRepository(
    val localDataSource: BookDataSource
) {
    private var cacheLawRefContainer: LawRefContainer? = null
    private var cacheGroupMap = mutableMapOf<String, LawRefContainer.Group>()
    private var cacheDocMap = mutableMapOf<String, Doc>()

    fun getSingleDoc(doc: Doc) : String {
        return localDataSource.getOriginText(doc)
    }

    fun getSingleLaw(doc: Doc): Law {
        return localDataSource.getLaw(doc)
    }

    fun getDoc(docId: String): Doc? {
        if (cacheDocMap.isEmpty()) getLawRefContainer()
        return cacheDocMap[docId]
    }

    fun getGroup(groupId: String): LawRefContainer.Group? {
        if (cacheGroupMap.isEmpty()) getLawRefContainer()
        return cacheGroupMap[groupId]
    }

    fun getLawRefContainer() : LawRefContainer {
        if (cacheLawRefContainer != null) return cacheLawRefContainer!!
        cacheLawRefContainer = localDataSource.getLawRefContainer()
        rebuildCacheMap()
        return cacheLawRefContainer!!
    }

    fun refreshLawRefContainer() : LawRefContainer {
        cacheLawRefContainer = localDataSource.getLawRefContainer()
        rebuildCacheMap()
        return cacheLawRefContainer!!
    }

    private fun rebuildCacheMap() {
        if (cacheLawRefContainer == null) return
        cacheGroupMap.clear()
        cacheDocMap.clear()
        cacheLawRefContainer!!.groupList.forEach { group ->
            cacheGroupMap[group.id] = group
            group.docList.forEach { doc ->
                cacheDocMap[doc.id] = doc
            }
        }
    }

    fun getLawByDocId(docId: String) : Result<Law> {
        val doc = getDoc(docId)
        if (doc == null) return Result.failure(IllegalArgumentException("找不到 $docId"))
        return Result.success(getSingleLaw(doc))
    }

}