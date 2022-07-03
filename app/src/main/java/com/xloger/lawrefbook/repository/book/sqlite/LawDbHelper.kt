package com.xloger.lawrefbook.repository.book.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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

    fun getTest() {
        openDatabase()
        XLog.d("start")
        db?.run {
            val cursor = rawQuery("SELECT * FROM category", null)
            cursor.moveToFirst()
            do {
                XLog.d(cursor.getString(1))
            } while (cursor.moveToNext())
        }
        XLog.d("end")
        close()
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