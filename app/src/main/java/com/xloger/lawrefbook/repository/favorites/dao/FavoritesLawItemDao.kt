package com.xloger.lawrefbook.repository.favorites.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.xloger.lawrefbook.repository.favorites.entity.FavoritesLawItem

/**
 * Created on 2022/3/29 12:02.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
@Dao
interface FavoritesLawItemDao {

    @Query("SELECT * FROM fav_item")
    fun getAll(): List<FavoritesLawItem>

    @Insert
    fun insert(item: FavoritesLawItem)

    @Delete
    fun delete(item: FavoritesLawItem)
}