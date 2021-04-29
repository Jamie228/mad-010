package com.sid1804492.bottomnavtest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [
    SchoolClass::class,
    Event::class,
    ToDo::class,
    UserOps::class,
    Wellbeing::class
], version = 7, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TeacherPlannerDatabase : RoomDatabase() {

    abstract val teacherPlannerDao: TeacherPlannerDao
    companion object {
        @Volatile
        private var INSTANCE: TeacherPlannerDatabase? = null

        fun getInstance(context: Context): TeacherPlannerDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TeacherPlannerDatabase::class.java,
                        "teacher_planner_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}