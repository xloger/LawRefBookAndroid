package com.xloger.lawrefbook.repository.book.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.xloger.lawrefbook.repository.book.dao.LawDataDbDao
import com.xloger.lawrefbook.repository.book.entity.menu.DateConverter
import com.xloger.lawrefbook.repository.book.entity.menu.LawDataDb

/**
 * Created on 2022/5/5 21:31.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
@Database(entities = [LawDataDb.Category::class, LawDataDb.Law::class], version = 2)
@TypeConverters(DateConverter::class)
abstract class LawDatabase: RoomDatabase() {
    abstract fun lawDao(): LawDataDbDao
}