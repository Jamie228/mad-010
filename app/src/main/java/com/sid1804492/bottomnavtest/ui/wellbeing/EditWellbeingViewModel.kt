package com.sid1804492.bottomnavtest.ui.wellbeing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.Wellbeing
import kotlinx.coroutines.launch

class EditWellbeingViewModel(
    private val wbId: Long,
    val dataSource: TeacherPlannerDao
) : ViewModel() {

    val wb = MutableLiveData<Wellbeing>()

    init {
        initWb()
    }

    private fun initWb() {
        viewModelScope.launch {
            wb.value = getWb()
        }
    }

    private suspend fun getWb(): Wellbeing {
        return dataSource.getWellbeing(wbId)
    }

    fun onUpdate(wb: Wellbeing) {
        viewModelScope.launch {
            updateWb(wb)
        }
    }

    private suspend fun updateWb(wb: Wellbeing) {
        dataSource.updateWellbeing(wb)
    }

}