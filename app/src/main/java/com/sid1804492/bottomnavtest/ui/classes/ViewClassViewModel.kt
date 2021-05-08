package com.sid1804492.bottomnavtest.ui.classes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sid1804492.bottomnavtest.database.SchoolClass
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import kotlinx.coroutines.launch
import java.util.*

class ViewClassViewModel(
    private val classId: Long,
    dataSource: TeacherPlannerDao
) : ViewModel() {

    //Get database and class
    val database = dataSource
    var curClass = MutableLiveData<SchoolClass?>()

    init {
        initClass()
    }

    val todos = database.getClassesTodos(classId)

    private fun initClass() {
        viewModelScope.launch {
            curClass.value = getClassFromDatabase(classId)
        }
    }

    private suspend fun getClassFromDatabase(classId: Long): SchoolClass? {
        return database.getClass(classId)
    }

    fun onDelete(classId: Long) {
        viewModelScope.launch {
            deleteClass(classId)
        }
    }

    private suspend fun deleteClass(classId: Long) {
        val sclass: SchoolClass? = database.getClass(classId)
        if (sclass != null) {
            database.deleteClass(sclass)
        }
    }

    fun onOldDelete() {
        val c: Calendar = Calendar.getInstance()
        val y = c.get(Calendar.YEAR)
        val m = c.get(Calendar.MONTH)
        val d = c.get(Calendar.DAY_OF_MONTH)
        c.clear()
        c.set(y, m, d)

        viewModelScope.launch {
            oldDelete(c)
        }
    }

    private suspend fun oldDelete(c: Calendar) {
        database.deleteOldTodos(c.timeInMillis)
    }
}