package com.example.tabata

import android.provider.BaseColumns
import androidx.annotation.ColorRes

object MyDbName {

    const val TABLE_NAME = "SEQUENCE_TABLE"
    const val COLUMN_1 = "title"
    const val COLUMN_2 = "color"
    const val COLUMN_3 = "warm_up_time"
    const val COLUMN_4 = "sets_number"
    const val COLUMN_5 = "workout_time"
    const val COLUMN_6 = "break_time"
    const val COLUMN_7 = "repetitions_number"
    const val COLUMN_8 = "long_break_time"

    const val DB_VERSION = 1
    const val DB_NAME = "my_db"
    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY AUTO_INCREMENT, " +
            "$COLUMN_1 TEXT," +
            "$COLUMN_2 INT UNSIGNED," +
            "$COLUMN_3 INT UNSIGNED,"+
            "$COLUMN_4 INT UNSIGNED," +
            "$COLUMN_5 INT UNSIGNED," +
            "$COLUMN_6 INT UNSIGNED," +
            "$COLUMN_7 INT UNSIGNED," +
            "$COLUMN_8 INT UNSIGNED)"

    val DELETE_TABLE: String? = "DROP TABLE IF EXISTS $TABLE_NAME "

}