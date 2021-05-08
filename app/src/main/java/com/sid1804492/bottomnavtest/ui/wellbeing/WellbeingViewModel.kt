package com.sid1804492.bottomnavtest.ui.wellbeing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao

class WellbeingViewModel(
    val dao: TeacherPlannerDao,
    application: Application
) : AndroidViewModel(application) {

    val wellbeings = dao.getAllWellbeing()

}