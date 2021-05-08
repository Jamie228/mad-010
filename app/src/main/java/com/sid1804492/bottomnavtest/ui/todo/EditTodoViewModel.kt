package com.sid1804492.bottomnavtest.ui.todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.ToDo
import kotlinx.coroutines.launch

class EditTodoViewModel(
    private val TodoId: Long,
    val dataSource: TeacherPlannerDao
) : ViewModel() {

    val todo = MutableLiveData<ToDo?>()

    init {
        initTodo(TodoId)
    }

    private fun initTodo(id: Long) {
        viewModelScope.launch {
            todo.value = getTodo(id)
        }
    }

    private suspend fun getTodo(id: Long): ToDo? {
        return dataSource.getTodo(id)
    }

    fun onUpdate(td: ToDo) {
        viewModelScope.launch {
            updateTodo(td)
        }
    }

    private suspend fun updateTodo(td: ToDo) {
        dataSource.updateTodo(td)
    }

}