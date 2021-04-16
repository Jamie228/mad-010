package com.sid1804492.bottomnavtest.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "classes_table")
data class SchoolClass (
    @PrimaryKey(autoGenerate = true)
    var ClassId: Long = 0L,

    @ColumnInfo(name = "set_name")
    var SetName: String,

    @ColumnInfo(name="subject_name")
    var SubjectName: String,

    @ColumnInfo(name ="year_group")
    var YearGroup: String,

    @ColumnInfo(name = "room")
    var Room: String
)