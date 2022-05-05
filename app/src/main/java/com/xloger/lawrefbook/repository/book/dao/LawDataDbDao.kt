package com.xloger.lawrefbook.repository.book.dao

import androidx.room.Dao
import androidx.room.Query
import com.xloger.lawrefbook.repository.book.entity.menu.LawDataDb

/**
 * Created on 2022/5/5 21:48.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
@Dao
interface LawDataDbDao {

    @Query("SELECT * FROM category")
    fun getCategory(): List<LawDataDb.Category>
}