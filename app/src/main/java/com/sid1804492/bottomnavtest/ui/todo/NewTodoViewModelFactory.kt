package com.sid1804492.bottomnavtest.ui.todo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao

class NewTodoViewModelFactory(
    private val dataSource: TeacherPlannerDao,
    private val classId: Long
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewTodoViewModel::class.java)) {
            return NewTodoViewModel(classId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}