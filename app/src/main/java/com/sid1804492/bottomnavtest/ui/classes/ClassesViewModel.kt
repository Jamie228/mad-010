package com.sid1804492.bottomnavtest.ui.classes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao

class ClassesViewModel(
    val database: TeacherPlannerDao,
    application: Application
) : AndroidViewModel(application) {

    val classes = database.getAllClasses()

}