package com.viranika.sepasgozari

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import java.io.File
import java.io.FileOutputStream

class DBHandler(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        val DB_NAME: String = "sepasgozari.db"
        val DB_VERSION: Int = 1
        var DB_PATH: String? = null
        val DB_TABLE_DAYS: String = "Days"
        val DB_TABLE_DAYS_CURRENT: String = "Days_current"
    }

    private var DB: SQLiteDatabase? = null

    init {
        DB_PATH = context.cacheDir.path + "/" + DB_NAME
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Not Code
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Not Code
    }

    private fun dbExists(): Boolean {
        val file = File(DB_PATH)
        return file.exists()
    }

    private fun copyDB(): Boolean {
        try {
            val fileOut = FileOutputStream(DB_PATH)
            val inStream = context.assets.open(DB_NAME)
            val buffer = ByteArray(1024)
            var ch = inStream.read(buffer)
            while (ch > 0) {
                fileOut.write(buffer, 0, ch)
                ch = inStream.read(buffer)
            }
            fileOut.flush()
            fileOut.close()
            inStream.close()
            return true

        } catch (ex: Exception) {
            return false
        }
    }

    fun openDB() {
        if (dbExists()) {
            try {
                val temp = File(DB_PATH)
                DB = SQLiteDatabase.openDatabase(temp.absolutePath, null, SQLiteDatabase.OPEN_READWRITE)
            } catch (ex: Exception) {

            }
        } else {
            if (copyDB())
                openDB()
            else
                return
        }
    }

    override fun close() {
        DB?.close()
    }

    @SuppressLint("Recycle")
    fun queryTblDays(): Cursor {
        return DB?.rawQuery("SELECT * FROM $DB_TABLE_DAYS", null)!!
    }

    @SuppressLint("Recycle")
    fun queryTblDayCurrent(): Cursor {
        return DB?.rawQuery("SELECT * FROM $DB_TABLE_DAYS_CURRENT", null)!!
    }

    @SuppressLint("Recycle")
    fun update(id: String, fav: String): Int? {
        val values = ContentValues()
        values.put("fav", fav)
        val selectionArgs = arrayOf(id)
        return DB?.update(DB_TABLE_DAYS, values, "DayID=?", selectionArgs)
    }

    @SuppressLint("Recycle")
    fun updateDayCurrent(dayCurrent: Int, dayOff: Int): Int? {
        val values = ContentValues()
        values.put("Day", dayCurrent)
        values.put("dayOff", dayOff)
        val selectionArgs = arrayOf("1")
        return DB?.update(DB_TABLE_DAYS_CURRENT, values, "ID=?", selectionArgs)
    }

    fun updateDayCurrentCast(dayCurrent: Int, dayOff: Int, term: Int): Int? {
        val values = ContentValues()
        values.put("Day", dayCurrent)
        values.put("dayOff", dayOff)
        values.put("term", term)
        val selectionArgs = arrayOf("1")
        return DB?.update(DB_TABLE_DAYS_CURRENT, values, "ID=?", selectionArgs)
    }

    fun runQuery(columns: Array<String>, selection: String, selectionArgs: Array<String>): Cursor {
        val qb = SQLiteQueryBuilder()
        qb.tables = DB_TABLE_DAYS
        return qb.query(DB, columns, selection, selectionArgs, null, null, null)
    }

}