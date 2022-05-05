package com.xloger.lawrefbook.repository.book.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import com.xloger.lawrefbook.repository.book.dao.LawDataDbDao
import com.xloger.lawrefbook.repository.book.entity.menu.LawDataDb
import java.util.*

/**
 * Created on 2022/5/5 21:31.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
@Database(entities = [LawDataDb.Category::class, LawDataDb.Law::class], version = 1)
abstract class LawDatabase: RoomDatabase() {
    abstract fun lawDao(): LawDataDbDao
}

class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return if (date == null) null else date.getTime()
    }
}