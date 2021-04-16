package com.sid1804492.bottomnavtest.ui.events

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import kotlinx.coroutines.launch
import java.util.*

class EventsViewModel(
    val database: TeacherPlannerDao,
    application: Application
) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Events Fragment"
    }
    val text: LiveData<String> = _text

    val events = database.getAllEvents()

    init {
        delOldEvents()
    }

    private fun delOldEvents() {
        viewModelScope.launch {
            val calendar: Calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            Log.d("CURDATE", day.toString() + "/" + month.toString() + "/" + year.toString())

            val nCalendar: Calendar = Calendar.getInstance()
            nCalendar.clear()
            nCalendar.set(year, month, day)
            Log.d("EVM - Current Date", nCalendar.timeInMillis.toString())

            delEv(nCalendar)
        }
    }

    private suspend fun delEv(ts: Calendar) {
        database.deleteOldEvents(ts.timeInMillis)
    }
}