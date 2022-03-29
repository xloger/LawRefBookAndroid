package com.xloger.lawrefbook.repository.favorites.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created on 2022/3/29 11:56.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
@Entity(tableName = "fav_item", indices = [Index(value = ["doc_id", "content"], unique = true)])
data class FavoritesLawItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "doc_id") val docId: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
) {

}