package com.example.newsreader.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsreader.data.local.room.converters.DateConverter
import com.example.newsreader.data.local.room.models.RoArticle
import com.example.newsreader.data.local.room.models.RoNewsSource

@Database(
    entities = arrayOf(RoNewsSource::class, RoArticle::class),
    version = 1)
@TypeConverters( DateConverter::class )
abstract class RoomNewsDatabase: RoomDatabase() {

    /**
     * Returns Dao for accessing Room database.
     *
     * @return Dao for accessing Room database.
     */
    abstract fun getNewsDao(): RoNewsDao
}
