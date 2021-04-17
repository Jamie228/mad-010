package com.sid1804492.bottomnavtest.ui.today

import android.app.Application
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.UserOps
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

    val locOp = MutableLiveData<UserOps?>()

    init {
        initLocSet()
    }

    val currentTime: LiveData<String> = _currentTime

    private fun initLocSet() {
       viewModelScope.launch{
           locOp.value = getLocSet()
       }
    }

    private suspend fun getLocSet(): UserOps? {
        return database.getLocOp()
    }

    fun onLocUpdate(item: UserOps, value: Boolean) {
        item.value = value
        viewModelScope.launch {
            updateLoc(item)
        }
    }

    private suspend fun updateLoc(item: UserOps) {
        database.updateUserOp(item)
    }

    fun createLoc(value: Boolean) {
        val loc = UserOps("LOCATION", value)
        viewModelScope.launch {
            insertLoc(loc)
        }
    }

    private suspend fun insertLoc(loc: UserOps) {
        database.insertUserOp(loc)
    }

}