package jp.ac.neec.it.k020c1302.schedule

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    //クラス内のprivate定数を宣言するためにcompanion objectブロックとする
    companion object{
        //DBファイルの定数
        private const val DATABASE_NAME = "schedule.db"
        //バージョン情報
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sqlSchedule = "CREATE TABLE schedule (_id INTEGER PRIMARY KEY, weekday TEXT, time TEXT, subject TEXT, room TEXT, things TEXT);"
        val sqlTime = "CREATE TABLE time (_id INTEGER PRIMARY KEY, time TEXT, hour INTEGER, minute INTEGER, notify INTEGER);"
        //実行
        db.execSQL(sqlSchedule)
        db.execSQL(sqlTime)
        initDataSet(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    private fun initDataSet(db: SQLiteDatabase){
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (1, 'monday', 'first', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (2, 'monday', 'second', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (3, 'monday', 'third', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (4, 'monday', 'fourth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (5, 'monday', 'fifth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (6, 'monday', 'sixth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (7, 'monday', 'seventh', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (8, 'tuesday', 'first', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (9, 'tuesday', 'second', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (10, 'tuesday', 'third', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (11, 'tuesday', 'fourth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (12, 'tuesday', 'fifth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (13, 'tuesday', 'sixth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (14, 'tuesday', 'seventh', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (15, 'wednesday', 'first', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (16, 'wednesday', 'second', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (17, 'wednesday', 'third', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (18, 'wednesday', 'fourth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (19, 'wednesday', 'fifth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (20, 'wednesday', 'sixth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (21, 'wednesday', 'seventh', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (22, 'thursday', 'first', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (23, 'thursday', 'second', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (24, 'thursday', 'third', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (25, 'thursday', 'fourth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (26, 'thursday', 'fifth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (27, 'thursday', 'sixth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (28, 'thursday', 'seventh', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (29, 'friday', 'first', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (30, 'friday', 'second', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (31, 'friday', 'third', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (32, 'friday', 'fourth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (33, 'friday', 'fifth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (34, 'friday', 'sixth', '', '', '')")
        db.execSQL("INSERT INTO schedule (_id, weekday, time, subject, room, things) VALUES (35, 'friday', 'seventh', '', '', '')")
        db.execSQL("INSERT INTO time (_id, time, hour, minute, notify) VALUES (1, 'first', 9, 0, 0)")
        db.execSQL("INSERT INTO time (_id, time, hour, minute, notify) VALUES (2, 'second', 10, 0, 0)")
        db.execSQL("INSERT INTO time (_id, time, hour, minute, notify) VALUES (3, 'third', 11, 0, 0)")
        db.execSQL("INSERT INTO time (_id, time, hour, minute, notify) VALUES (4, 'fourth', 13, 0, 0)")
        db.execSQL("INSERT INTO time (_id, time, hour, minute, notify) VALUES (5, 'fifth', 14, 0, 0)")
        db.execSQL("INSERT INTO time (_id, time, hour, minute, notify) VALUES (6, 'sixth', 15, 0, 0)")
        db.execSQL("INSERT INTO time (_id, time, hour, minute, notify) VALUES (7, 'seventh', 16, 0, 0)")
    }
}