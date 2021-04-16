package com.sid1804492.bottomnavtest.ui.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.ToDo
import kotlinx.coroutines.launch
import java.util.*

class NewTodoViewModel(private val class_id: Long, val database: TeacherPlannerDao): ViewModel() {

    private val calendar: Calendar = Calendar.getInstance();
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    private val _dateInfo = MutableLiveData<Map<String, Int>>().apply {
        value = mapOf("year" to year, "month" to month, "day" to day)
    }

    val dateInfo: LiveData<Map<String, Int>>
        get() = _dateInfo

    fun onSave(todo_type: String, todo_date: Calendar, todo_text: String) {
        viewModelScope.launch {
            val newTodo: ToDo = ToDo(ClassId = class_id, TodoType = todo_type, TodoDate = todo_date, TodoText = todo_text, TodoComplete = false)
            insert(newTodo)
        }
    }

    private suspend fun insert(nt: ToDo) {
        database.insertTodo(nt)
    }

}