package com.sid1804492.bottomnavtest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "wellbeing_table")
data class Wellbeing(

    @PrimaryKey(autoGenerate = true)
    val WellbeingId: Long = 0L,

    @ColumnInfo(name = "date")
    val date: Calendar,

    @ColumnInfo(name = "rating")
    var rating: Int,

    @ColumnInfo(name = "went_well")
    var wentWell: String,

    @ColumnInfo(name = "improve_on")
    var improveOn: String,

    @ColumnInfo(name = "anything_else")
    var anythingElse: String?
)