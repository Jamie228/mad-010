package com.sid1804492.bottomnavtest.ui.wellbeing

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import java.lang.IllegalArgumentException

class WellbeingViewModelFactory(
    private val dao: TeacherPlannerDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WellbeingViewModel::class.java)) {
            return WellbeingViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}