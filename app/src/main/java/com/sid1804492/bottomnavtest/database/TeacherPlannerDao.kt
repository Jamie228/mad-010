package com.sid1804492.bottomnavtest.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface TeacherPlannerDao {

    //INSERT STATEMENTS
    @Insert
    suspend fun insertClass(schoolClass: SchoolClass)

    @Insert
    suspend fun insertEvent(event: Event)

    @Insert
    suspend fun insertTodo(todo: ToDo)

    //UPDATE STATEMENTS
    @Update
    suspend fun updateClass(schoolClass: SchoolClass)

    @Update
    suspend fun updateEvent(event: Event)

    @Update
    suspend fun updateTodo(todo: ToDo)

    //DELETE STATEMENTS
    @Delete
    suspend fun deleteClass(schoolClass: SchoolClass)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Delete
    suspend fun deleteTodo(todo: ToDo)

    @Query("DELETE FROM event_table WHERE event_date < :current_date")
    suspend fun deleteOldEvents(current_date: Long)

    @Query("DELETE FROM todo_table WHERE todo_date < :current_date")
    suspend fun deleteOldTodos(current_date: Long)

    //GET STATEMENTS
    @Query("SELECT * FROM classes_table WHERE ClassId = :key")
    suspend fun getClass(key: Long): SchoolClass?

    @Query("SELECT * FROM event_table WHERE EventId = :key")
    suspend fun getEvent(key: Int): Event?

    @Query("SELECT * FROM classes_table")
    fun getAllClasses(): LiveData<List<SchoolClass>>

    @Query("SELECT * FROM event_table ORDER BY event_date ASC")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM todo_table WHERE class_id = :class_id ORDER BY todo_date ASC")
    fun getClassesTodos(class_id: Long): LiveData<List<ToDo>>

    @Query("SELECT todo_text AS todoText, set_name AS setName FROM todo_table INNER JOIN classes_table WHERE todo_table.todo_type = 'To-Do' AND todo_table.todo_date = :today")
    fun getTodayTodos(today: Long): LiveData<List<CTodo>>

    data class CTodo(val setName:String, val todoText: String)

}