package com.xloger.lawrefbook.repository.favorites.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xloger.lawrefbook.repository.favorites.dao.FavoritesLawItemDao
import com.xloger.lawrefbook.repository.favorites.entity.FavoritesLawItem

/**
 * Created on 2022/3/29 12:12.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
@Database(entities = [FavoritesLawItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favItemDao(): FavoritesLawItemDao
}