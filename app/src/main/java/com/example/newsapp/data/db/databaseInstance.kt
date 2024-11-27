package com.example.newsapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.newsapp.data.model.Article
import com.example.newsapp.util.constant
import com.example.newsapp.util.constant.Companion.DATA_BASE_NAME

@Database(entities = [Article::class], version = 2, exportSchema = false)
@TypeConverters(Converter::class)
abstract class databaseInstance : RoomDatabase() {
    abstract fun newsDAO(): NewsDAO

    companion object {
        @Volatile
        private var INSTANCE: databaseInstance? = null

        fun getDatabaseInstance(context: Context): databaseInstance {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    databaseInstance::class.java,
                  DATA_BASE_NAME
                ).build()
                INSTANCE = instance
                instance// this is the database instance that we return

            }


        }


    }
}