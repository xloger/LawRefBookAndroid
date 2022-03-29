package com.xloger.lawrefbook.repository.favorites.dao

import androidx.room.*
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: FavoritesLawItem)

    @Delete
    fun delete(item: FavoritesLawItem)

    @Query("SELECT * FROM fav_item WHERE doc_id LIKE :docId AND content LIKE :content")
    fun filterDocAndContent(docId: String, content: String): List<FavoritesLawItem>
}