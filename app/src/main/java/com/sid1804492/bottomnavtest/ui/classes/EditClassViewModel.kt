package com.sid1804492.bottomnavtest.ui.classes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sid1804492.bottomnavtest.database.SchoolClass
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import kotlinx.coroutines.launch

class EditClassViewModel(
    private val classId: Long,
    val dataSource: TeacherPlannerDao
) : ViewModel() {

    val curClass = MutableLiveData<SchoolClass>()

    init {
        initClass(classId)
    }

    private fun initClass(id: Long) {
        viewModelScope.launch {
            curClass.value = getClass(id)
        }
    }

    private suspend fun getClass(id: Long) : SchoolClass? {
        return dataSource.getClass(id)
    }

    fun onUpdate(sc: SchoolClass) {
        viewModelScope.launch {
            updateClass(sc)
        }
    }

    private suspend fun updateClass(sc: SchoolClass) {
        dataSource.updateClass(sc)
    }

}