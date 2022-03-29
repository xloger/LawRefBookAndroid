package com.xloger.lawrefbook.repository.favorites

import com.xloger.lawrefbook.repository.favorites.database.AppDatabase
import com.xloger.lawrefbook.repository.favorites.entity.FavoritesLawItem

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

    fun removeFavItem(item: FavoritesLawItem) = itemDao.delete(item)
}