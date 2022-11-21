package com.example.tabata.Db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.SequenceModel

@Database(entities = [SequenceModel::class, PhaseModel::class], version = 13)
abstract class MyDb: RoomDatabase() {
    abstract fun getDao(): Dao
    companion object{
        fun getDb(context: Context): MyDb {
            return Room.databaseBuilder(
                context.applicationContext,
                MyDb::class.java,
                "tabata.db"
            ).fallbackToDestructiveMigration()
                .build()

            Log.d("debug","getDb")
        }
    }
}