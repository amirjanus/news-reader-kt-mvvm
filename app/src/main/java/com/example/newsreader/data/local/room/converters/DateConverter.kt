package com.example.newsreader.data.local.room.converters

import androidx.room.TypeConverter
import java.util.*

/**
 * Converter for Room database.
 */
class DateConverter {

    /**
     * Converts time in milliseconds to Date object.
     *
     * @param millis Time in milliseconds since epoch.
     * @return Date object.
     */
    @TypeConverter
    fun fromMillis(millis: Long): Date {
        return Date(millis)
    }

    /**
     * Converts Date object to time in milliseconds.
     *
     * @param date Date object.
     * @return Time in milliseconds since epoch.
     */
    @TypeConverter
    fun toMillis(date: Date): Long {
        return date.time
    }

}
