package com.sid1804492.bottomnavtest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "event_table")
data class Event (
    @PrimaryKey(autoGenerate = true)
    var EventId: Long = 0L,

    @ColumnInfo(name = "event_name")
    var EventName: String,

    @ColumnInfo(name = "event_date")
    var EventDate: Calendar,

    @ColumnInfo(name = "event_text")
    var EventText: String
)