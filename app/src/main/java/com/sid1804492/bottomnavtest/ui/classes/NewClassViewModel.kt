package com.sid1804492.bottomnavtest.ui.classes

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sid1804492.bottomnavtest.database.SchoolClass
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import kotlinx.coroutines.launch

class NewClassViewModel(val database: TeacherPlannerDao, application: Application) :
    AndroidViewModel(application) {

    fun onSave(set: String, name: String, year: String, room: String) {
        viewModelScope.launch {
            val newClass =
                SchoolClass(Room = room, SubjectName = name, SetName = set, YearGroup = year)
            insert(newClass)
        }
    }

    private suspend fun insert(sclass: SchoolClass) {
        database.insertClass(sclass)
    }

}