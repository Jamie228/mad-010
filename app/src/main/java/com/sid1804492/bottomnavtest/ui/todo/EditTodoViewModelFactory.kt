package com.sid1804492.bottomnavtest.ui.todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao

class EditTodoViewModelFactory(
    private val todoId: Long,
    private val dataSource: TeacherPlannerDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTodoViewModel::class.java)) {
            return EditTodoViewModel(todoId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}