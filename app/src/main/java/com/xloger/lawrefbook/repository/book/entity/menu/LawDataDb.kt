package com.xloger.lawrefbook.repository.book.entity.menu

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.xloger.lawrefbook.util.XLog

/**
 * Created on 2022/5/5 21:24.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class LawDataDb {

    @Entity(tableName = "category")
    data class Category(
        @PrimaryKey val id: Int,
        @ColumnInfo val name: String,
        @ColumnInfo val folder: String,
        @ColumnInfo val isSubFolder: Int,
        @ColumnInfo val group: String?,
        @ColumnInfo val order: Int?
    )

    @Entity(tableName = "law",
        foreignKeys = arrayOf(ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("category_id")
        )),
        indices = [
            Index(name = "law_category_id", value = ["category_id"], unique = false),
            Index(name = "law_name", value = ["name"], unique = false),
            Index(name = "index_law_category_id_name", value = ["category_id", "name"], unique = false),
        ],
    )
    data class Law(
        @PrimaryKey val id: String,
        @ColumnInfo val level: String,
        @ColumnInfo val name: String,
        @ColumnInfo(name = "filename") val fileName: String?,
        @ColumnInfo() val publish: String? = null,
        @ColumnInfo val expired: Int,
        @ColumnInfo(name = "category_id") val categoryId: Int,
        @ColumnInfo val order: Int?,
        @ColumnInfo(name = "subtitle") val subTitle: String?,
        @ColumnInfo(name = "valid_from") val validFrom: String? = null
    )
}

class LawDataMjigration : Migration(0, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        XLog.e("start migrate")
    }

}