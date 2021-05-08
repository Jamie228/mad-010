package com.sid1804492.bottomnavtest.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao

class EditEventViewModelFactory(
    private val eventId: Long,
    private val dataSource: TeacherPlannerDao
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditEventViewModel::class.java)) {
            return EditEventViewModel(eventId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}