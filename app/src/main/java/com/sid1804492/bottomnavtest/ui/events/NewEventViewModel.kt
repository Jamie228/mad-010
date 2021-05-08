package com.sid1804492.bottomnavtest.ui.events

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.sid1804492.bottomnavtest.database.Event
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import kotlinx.coroutines.launch
import java.util.*

class NewEventViewModel(
    val database: TeacherPlannerDao,
    application: Application
) : AndroidViewModel(application) {

    val calendar: Calendar = Calendar.getInstance();
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    private val _dateInfo = MutableLiveData<Map<String, Int>>().apply {
        value = mapOf("year" to year, "month" to month, "day" to day)
    }

    val dateInfo: LiveData<Map<String, Int>>
        get() = _dateInfo

    fun onSave(eventName: String, eventDate: Calendar, eventText: String) {
        viewModelScope.launch {
            Log.d("NEVM - Selected Date", eventDate.timeInMillis.toString())
            val newEvent =
                Event(EventName = eventName, EventDate = eventDate, EventText = eventText)
            insert(newEvent)
        }
    }

    private suspend fun insert(newEvent: Event) {
        database.insertEvent(newEvent)
    }

}