package com.sid1804492.bottomnavtest.ui.today_tabs

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import java.lang.IllegalArgumentException

class TodayEventViewModelFactory(
    private val dataSource: TeacherPlannerDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TodayEventViewModel::class.java)) {
            return TodayEventViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}