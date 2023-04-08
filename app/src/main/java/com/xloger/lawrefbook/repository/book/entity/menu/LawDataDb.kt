package com.xloger.lawrefbook.repository.book.entity.menu

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.xloger.lawrefbook.util.XLog

/**
 * Created on 2022/5/5 21:24.
 * Author: xloger
 * Email:phoenix@xloger.com
 * 与 `Laws/db.sqlite3` 一一对应的数据结构
 */
class LawDataDb {

    /**
     * 对应 `Laws/db.sqlite3` 中的 `category` 表
     * @param id 分类的id
     * @param name 分类的名称
     * @param folder 分类所属的文件夹。可能为二级目录，例如 "地方性法规/上海"
     * @param isSubFolder 是否为二级目录, 0 表示不是，1 表示是。为 1 则 [group] 有值，为其一级目录
     * @param group 一级目录的名称。如果 [isSubFolder] 为 0，则该值为 null
     * @param order 排序的顺序。数字越小越靠前
     * Ps:当 [isSubFolder] 为 1 时，[group] 为 null 时，表示该分类为一级目录
     */
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
    /**
     * 对应 `Laws/db.sqlite3` 中的 `law` 表
     * @param id 法律法规的 UUID
     * @param level
     * @param name 法律法规的名称
     * @param fileName 法律法规的文件名，为 null 则使用 [name]
     * @param publish 发布日期
     * @param expired 是否过期，0 表示未过期，1 表示已过期
     * @param categoryId 法律法规所属的分类的 id
     * @param order 排序的顺序。可能为 null
     * @param subTitle 副标题。可能为 null
     * @param validFrom 生效日期。可能为 null
     * @param ver 版本号。
     */
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

@Deprecated("该类因为 ROOM 无法映射 DATE 类型，废弃")
class LawDataMigration : Migration(0, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        XLog.e("start migrate")
    }

}