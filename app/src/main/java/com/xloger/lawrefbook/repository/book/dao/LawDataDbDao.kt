package com.xloger.lawrefbook.repository.book.dao

import androidx.room.Dao
import androidx.room.Query
import com.xloger.lawrefbook.repository.book.entity.menu.LawDataDb

/**
 * Created on 2022/5/5 21:48.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
@Deprecated("该类因为 ROOM 无法映射 DATE 类型，废弃")
@Dao
interface LawDataDbDao {

    @Query("SELECT * FROM category")
    fun getCategory(): List<LawDataDb.Category>
}