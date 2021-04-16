package com.sid1804492.bottomnavtest.database

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long): Calendar {
        val c: Calendar = Calendar.getInstance()
        c.timeInMillis = value
        return c
    }

    @TypeConverter
    fun calendarToTimestamp(c: Calendar?): Long? {
        return c?.time?.time
    }

}