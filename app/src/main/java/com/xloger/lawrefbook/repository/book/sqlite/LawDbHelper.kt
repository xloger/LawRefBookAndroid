package com.xloger.lawrefbook.repository.book.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import com.xloger.lawrefbook.repository.book.entity.menu.LawDataDb
import com.xloger.lawrefbook.util.XLog
import java.io.File
import java.io.FileOutputStream

/**
 * Created on 2022/7/3 23:19.
 * Author: xloger
 * Email:phoenix@xloger.com
 * Tip:SQLiteDatabase 不支持打开 assets 中的数据库，只能复制到 data/data/包名/files/ 目录下
 */
class LawDbHelper(val context: Context): SQLiteOpenHelper(context, Name, null, Version) {
    private val AssetsPath = "Laws/db.sqlite3"
    private val DbFile = File(context.filesDir, "LawDb.sqlite3")

    private var db: SQLiteDatabase? = null

    init {
//        val isExist = checkDatabase() //TODO 未考虑更新的情况
//        if (!isExist) {
//            createDatabase()
//        }
        //TODO 将来根据版本号判断是否更新吧
        createDatabase()
    }

    private fun checkDatabase() : Boolean {
        return DbFile.exists()
    }

    private fun createDatabase() {
        try {
            val inputStream = context.assets.open(AssetsPath)
            inputStream.use {
                val outputStream = FileOutputStream(DbFile)
                inputStream.copyTo(outputStream)
                outputStream.flush()
                outputStream.close()
            }
        } catch (ex: Throwable) {
            XLog.e(ex)
        }
    }

    private fun openDatabase() {
        db = SQLiteDatabase.openDatabase(DbFile.path, null, SQLiteDatabase.OPEN_READONLY)
    }

    override fun close() {
        db?.close()
        super.close()
    }

    fun getCategory(): List<LawDataDb.Category> {
        val list = mutableListOf<LawDataDb.Category>()
        openDatabase()
        db?.run {
            val cursor = rawQuery("SELECT * FROM category", null)
            cursor.moveToFirst()
            do {
                cursor.run {
                    val id = getString(cursor.getColumnIndexOrThrow("id"))
                    val name = getString(cursor.getColumnIndexOrThrow("name"))
                    val folder = getString(cursor.getColumnIndexOrThrow("folder"))
                    val isSubFolder = getInt(cursor.getColumnIndexOrThrow("isSubFolder"))
                    val group = getStringOrNull(cursor.getColumnIndexOrThrow("group"))
                    val order = getInt(cursor.getColumnIndexOrThrow("order"))
                    list.add(LawDataDb.Category(id, name, folder, isSubFolder, group, order))
                }
            } while (cursor.moveToNext())
        }
        close()
        return list
    }

    fun getLaw(): List<LawDataDb.Law> {
        val list = mutableListOf<LawDataDb.Law>()
        openDatabase()
        db?.run {
            val cursor = rawQuery("SELECT * FROM law", null)
            cursor.moveToFirst()
            do {
                cursor.run {
                    val id = getString(cursor.getColumnIndexOrThrow("id"))
                    val level = getString(cursor.getColumnIndexOrThrow("level"))
                    val name = getString(cursor.getColumnIndexOrThrow("name"))
                    val fileName = getStringOrNull(cursor.getColumnIndexOrThrow("filename"))
                    val publish = getStringOrNull(cursor.getColumnIndexOrThrow("publish")).let { if (it.isNullOrBlank() == true) null else it }
                    val expired = getInt(cursor.getColumnIndexOrThrow("expired"))
                    val categoryId = getString(cursor.getColumnIndexOrThrow("category_id"))
                    val order = getIntOrNull(cursor.getColumnIndexOrThrow("order"))
                    val subTitle = getStringOrNull(cursor.getColumnIndexOrThrow("subtitle"))
                    val validFrom = getStringOrNull(cursor.getColumnIndexOrThrow("valid_from"))
                    list.add(LawDataDb.Law(id, level, name, fileName, publish, expired, categoryId, order, subTitle, validFrom))
                }
            } while (cursor.moveToNext())
        }
        close()
        return list
    }

    override fun onCreate(p0: SQLiteDatabase?) {

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    companion object {
        const val Version = 1
        const val Name = "db.sqlite3"
    }
}