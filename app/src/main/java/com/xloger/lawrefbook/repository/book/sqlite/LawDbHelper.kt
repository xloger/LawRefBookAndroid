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
 */
class LawDbHelper(val context: Context): SQLiteOpenHelper(context, Name, null, Version) {
    private val AssetsPath = "Laws/db.sqlite3"
    private val DbFile = File(context.filesDir, "LawDb.sqlite3")

    private var db: SQLiteDatabase? = null

    init {
        val isExist = checkDatabase() //TODO 未考虑更新的情况
        if (isExist) {

        } else {
            createDatabase()
        }
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
        XLog.d("start")
        db?.run {
            val cursor = rawQuery("SELECT * FROM category", null)
            cursor.moveToFirst()
            do {
                cursor.run {
                    val id = getInt(0)
                    val name = getString(1)
                    val folder = getString(3)
                    val isSubFolder = getInt(4)
                    val group = getStringOrNull(5)
                    val order = getIntOrNull(6)
                    list.add(LawDataDb.Category(id, name, folder, isSubFolder, group, order))
                }
            } while (cursor.moveToNext())
        }
        XLog.d("end")
        close()
        return list
    }

    fun getLaw(): List<LawDataDb.Law> {
        val list = mutableListOf<LawDataDb.Law>()
        openDatabase()
        XLog.d("start")
        db?.run {
            val cursor = rawQuery("SELECT * FROM law", null)
            cursor.moveToFirst()
            do {
                cursor.run {
                    val id = getString(0)
                    val level = getString(1)
                    val name = getString(2)
                    val fileName = getStringOrNull(3)
                    val publish = getStringOrNull(4)
                    val expired = getInt(5)
                    val categoryId = getInt(6)
                    val order = getIntOrNull(7)
                    val subTitle = getStringOrNull(8)
                    val validFrom = getStringOrNull(9)
                    list.add(LawDataDb.Law(id, level, name, fileName, publish, expired, categoryId, order, subTitle, validFrom))
                }
            } while (cursor.moveToNext())
        }
        XLog.d("end")
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