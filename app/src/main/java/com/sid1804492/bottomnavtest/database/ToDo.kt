package com.sid1804492.bottomnavtest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "todo_table", foreignKeys = [ForeignKey(entity = SchoolClass::class,
    parentColumns = arrayOf("ClassId"), childColumns = arrayOf("class_id"), onDelete = ForeignKey.CASCADE)]
)
data class ToDo (
    @PrimaryKey(autoGenerate = true)
    val TodoId: Long = 0L,

    @ColumnInfo(name = "class_id")
    val ClassId: Long,

    @ColumnInfo(name = "todo_type")
    val TodoType: String,

    @ColumnInfo(name = "todo_date")
    val TodoDate: Calendar,

    @ColumnInfo(name = "todo_text")
    val TodoText: String,

    @ColumnInfo(name = "todo_complete")
    val TodoComplete: Boolean
)