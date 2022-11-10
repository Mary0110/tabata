package com.example.tabata

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase


class MyDbManager(context: Context) {
    val dbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null
    val cont = context
    fun openDb (){
        db = dbHelper.writableDatabase
    }

    fun insertIntoDb(title: String, color: String, warm_up_time: String, sets_number: Int,
    workout_time: String, break_time: String, repetitions_number: Int, long_break_time:String)
    {
        val values = ContentValues().apply{
            put(MyDbName.COLUMN_1, title)
            put(MyDbName.COLUMN_2, color)
            put(MyDbName.COLUMN_3, warm_up_time)
            put(MyDbName.COLUMN_4, sets_number)
            put(MyDbName.COLUMN_5, workout_time)
            put(MyDbName.COLUMN_6, break_time)
            put(MyDbName.COLUMN_7, repetitions_number)
            put(MyDbName.COLUMN_8, long_break_time)
        }
        db?.insert(MyDbName.TABLE_NAME, null, values)

    }


    fun readDbData() : ArrayList<SequenceModel>{
        val dataList = ArrayList<SequenceModel>()
        val cursor = db?.query(MyDbName.TABLE_NAME, null, null, null, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()){
                dataList.add(
                    SequenceModel(
                        cursor.getString(cursor.getColumnIndexOrThrow(MyDbName.COLUMN_1)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MyDbName.COLUMN_2)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MyDbName.COLUMN_3)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MyDbName.COLUMN_4)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MyDbName.COLUMN_5)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MyDbName.COLUMN_6)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MyDbName.COLUMN_7)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(MyDbName.COLUMN_8))
                    )
                )
            }
        }
        if (cursor != null) {
            cursor.close()
        }
         return  dataList
    }


    fun closeDb(){
        db?.close()
    }
}