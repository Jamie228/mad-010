package com.sid1804492.bottomnavtest.ui.events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sid1804492.bottomnavtest.database.Event
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import kotlinx.coroutines.launch

class EditEventViewModel(
    private val eventId: Long,
    val dataSource: TeacherPlannerDao
) : ViewModel() {

    val event = MutableLiveData<Event?>()

    init {
        initEvent(eventId)
    }

    private fun initEvent(id: Long) {
        viewModelScope.launch {
            event.value = getEvent(id)
        }
    }

    private suspend fun getEvent(id: Long) : Event? {
        return dataSource.getEvent(id)
    }

    fun onUpdate(event: Event) {
        viewModelScope.launch {
            updateEvent(event)
        }
    }

    private suspend fun updateEvent(event: Event) {
        dataSource.updateEvent(event)
    }

}