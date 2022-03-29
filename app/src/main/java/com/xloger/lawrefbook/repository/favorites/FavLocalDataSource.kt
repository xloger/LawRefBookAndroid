package com.xloger.lawrefbook.repository.favorites

import com.xloger.lawrefbook.repository.favorites.database.AppDatabase
import com.xloger.lawrefbook.repository.favorites.entity.FavoritesLawItem
import com.xloger.lawrefbook.util.XLog

/**
 * Created on 2022/3/29 12:16.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class FavLocalDataSource(
    private val db: AppDatabase
) {
    private val itemDao = db.favItemDao()

    fun getAllFavItem(): List<FavoritesLawItem> {
        return itemDao.getAll()
    }

    fun addFavItem(item: FavoritesLawItem) = itemDao.insert(item)

    fun removeFavItem(docId: String, content: String) {
        val list = itemDao.filterDocAndContent(docId, content)
        if (list.isEmpty()) {
            XLog.e("要删除的内容 $content 未找到")
            return
        }
        list.forEach {
            itemDao.delete(it)
        }
    }
}