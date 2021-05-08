package com.sid1804492.bottomnavtest.ui.classes

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao

class NewClassViewModelFactory(
    private val dataSource: TeacherPlannerDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewClassViewModel::class.java)) {
            return NewClassViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}