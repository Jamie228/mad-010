package com.sid1804492.bottomnavtest.ui.wellbeing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.Wellbeing
import kotlinx.coroutines.launch

class NewWellbeingViewModel(
    val database: TeacherPlannerDao,
    application: Application
) : AndroidViewModel(application) {

    fun onSave(wb: Wellbeing) {
        viewModelScope.launch {
            saveWb(wb)
        }
    }

    private suspend fun saveWb(wb: Wellbeing) {
        database.insertWellbeing(wb)
    }

}