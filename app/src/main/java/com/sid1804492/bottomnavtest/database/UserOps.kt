package com.sid1804492.bottomnavtest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_options")
data class UserOps(
    @PrimaryKey
    var name: String,
    @ColumnInfo(name = "value")
    var value: Boolean
)