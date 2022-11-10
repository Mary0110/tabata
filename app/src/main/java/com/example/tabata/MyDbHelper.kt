package com.example.tabata

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDbHelper(context : Context): SQLiteOpenHelper(context, MyDbName.DB_NAME, null, MyDbName.DB_VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(MyDbName.CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(MyDbName.DELETE_TABLE)
        onCreate(p0)
    }
}