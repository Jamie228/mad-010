package com.sid1804492.bottomnavtest.ui.today

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.time.Year

class TodayViewModel(val database: TeacherPlannerDao, application: Application) :
    AndroidViewModel(application) {

    private val _currentTime = MutableLiveData<String>().apply {
        value = SimpleDateFormat("EEEE d MMMM", java.util.Locale.getDefault()).format(
            java.util.Date()
        )
    }

    val currentTime: LiveData<String> = _currentTime
}