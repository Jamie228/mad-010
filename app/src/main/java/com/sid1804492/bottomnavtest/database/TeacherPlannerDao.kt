package com.sid1804492.bottomnavtest.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*
import java.util.concurrent.Flow

@Dao
interface TeacherPlannerDao {

    //INSERT STATEMENTS
    @Insert
    suspend fun insertClass(schoolClass: SchoolClass)

    @Insert
    suspend fun insertEvent(event: Event)

    @Insert
    suspend fun insertTodo(todo: ToDo)

    @Insert
    suspend fun insertUserOp(userOp: UserOps)

    @Insert
    suspend fun insertWellbeing(wb: Wellbeing)

    //UPDATE STATEMENTS
    @Update
    suspend fun updateClass(schoolClass: SchoolClass)

    @Update
    suspend fun updateEvent(event: Event)

    @Update
    suspend fun updateTodo(todo: ToDo)

    @Update
    suspend fun updateUserOp(userOp: UserOps)

    @Update
    suspend fun updateWellbeing(wb: Wellbeing)

    @Query("UPDATE event_table SET complete = 1 WHERE EventId = :id")
    suspend fun completeEvent(id: Long)

    @Query("UPDATE event_table SET complete = 0 WHERE EventId = :id")
    suspend fun incompleteEvent(id: Long)

    @Query("UPDATE todo_table SET todo_complete = 1 WHERE TodoId = :id")
    suspend fun completeTodo(id: Long)

    @Query("UPDATE todo_table SET todo_complete = 0 WHERE TodoId = :id")
    suspend fun incompleteTodo(id: Long)

    //DELETE STATEMENTS
    @Delete
    suspend fun deleteClass(schoolClass: SchoolClass)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Delete
    suspend fun deleteTodo(todo: ToDo)

    @Delete
    suspend fun deleteWellbeing(wb: Wellbeing)

    @Query("DELETE FROM event_table WHERE event_date < :current_date")
    suspend fun deleteOldEvents(current_date: Long)

    @Query("DELETE FROM todo_table WHERE todo_date < :current_date")
    suspend fun deleteOldTodos(current_date: Long)

    @Query("DELETE FROM todo_table WHERE TodoId = :id")
    suspend fun deleteTodoWithId(id: Long)

    @Query("DELETE FROM event_table WHERE EventId = :id")
    suspend fun deleteEventWithId(id: Long)

    //GET STATEMENTS
    @Query("SELECT * FROM user_options WHERE name = 'LOCATION'")
    suspend fun getLocOp(): UserOps?

    @Query("SELECT * FROM classes_table WHERE ClassId = :key")
    suspend fun getClass(key: Long): SchoolClass?

    @Query("SELECT * FROM event_table WHERE EventId = :key")
    suspend fun getEvent(key: Long): Event?

    @Query("SELECT * FROM classes_table")
    fun getAllClasses(): LiveData<List<SchoolClass>>

    @Query("SELECT * FROM event_table ORDER BY event_date ASC")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM todo_table WHERE class_id = :class_id ORDER BY todo_date ASC")
    fun getClassesTodos(class_id: Long): LiveData<List<ToDo>>

    @Query("SELECT todo_text AS todoText, set_name AS setName, todo_complete AS complete, TodoId AS id FROM todo_table INNER JOIN classes_table ON todo_table.class_id = classes_table.ClassId WHERE todo_table.todo_type = 'To-Do' AND todo_table.todo_date = :today")
    fun getTodayTodos(today: Long): LiveData<List<CTodo>>
    data class CTodo(val setName:String, val todoText: String, val complete: Boolean, val id: Long)

    @Query("SELECT todo_text AS todoText, set_name AS setName, todo_complete AS complete, TodoId AS id FROM todo_table INNER JOIN classes_table ON todo_table.class_id = classes_table.ClassId WHERE todo_table.todo_type = 'Homework' AND todo_table.todo_date = :today")
    fun getTodayHomework(today:Long): LiveData<List<CHomework>>
    data class CHomework(val setName:String, val todoText: String, val complete: Boolean, val id: Long)

    @Query("SELECT * FROM event_table WHERE event_date = :today ORDER BY complete")
    fun getTodayEvent(today:Long): LiveData<List<Event>>

    @Query("SELECT COUNT(*) FROM event_table WHERE event_date = :today")
    suspend fun getTodayEventCount(today: Long) : Int

    @Query("SELECT COUNT(*) FROM todo_table WHERE todo_date = :today AND todo_type = 'To-Do'")
    suspend fun getTodayTodoCount(today: Long) : Int

    @Query("SELECT COUNT(*) FROM todo_table WHERE todo_date = :today AND todo_type = 'Homework'")
    suspend fun getTodayHomeworkCount(today: Long) : Int

    @Query("SELECT * FROM wellbeing_table")
    fun getAllWellbeing(): LiveData<List<Wellbeing>>

//    @Query("SELECT * FROM wellbeing_table ORDER BY date DESC LIMIT 7")
//    suspend fun wellbeingRecentSeven(): LiveData<List<Wellbeing>>

}