package com.sid1804492.bottomnavtest.ui.classes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao

class EditClassViewModelFactory(
    private val classId: Long,
    private val dataSource: TeacherPlannerDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditClassViewModel::class.java)) {
            return EditClassViewModel(classId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}