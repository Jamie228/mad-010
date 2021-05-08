package com.sid1804492.bottomnavtest.ui.wellbeing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao

class EditWellbeingViewModelFactory(
    private val wbId: Long,
    private val dataSource: TeacherPlannerDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditWellbeingViewModel::class.java)) {
            return EditWellbeingViewModel(wbId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}