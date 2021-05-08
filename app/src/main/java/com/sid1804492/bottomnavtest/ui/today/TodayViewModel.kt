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
import java.util.*

class TodayViewModel(val database: TeacherPlannerDao, application: Application) :
    AndroidViewModel(application) {

    private var c: Calendar = Calendar.getInstance()

    private val _currentTime = MutableLiveData<String>().apply {
        value = SimpleDateFormat("EEEE d MMMM", java.util.Locale.getDefault()).format(
            java.util.Date()
        )
    }

    val locOp = MutableLiveData<UserOps?>()

    init {
        //Store day month year, clear to clear time val and reset with values
        val d = c.get(Calendar.DAY_OF_MONTH)
        val m = c.get(Calendar.MONTH)
        val y = c.get(Calendar.YEAR)
        c.clear()
        c.set(y, m, d)
        initLocSet()
    }

    //Get counts
    val eventNo = database.getTodayEventCount(c.timeInMillis)
    val todoNo = database.getTodayTodoCount(c.timeInMillis)
    val homeworkNo = database.getTodayHomeworkCount(c.timeInMillis)

    val currentTime: LiveData<String> = _currentTime

    //Get location setting
    private fun initLocSet() {
        viewModelScope.launch {
            locOp.value = getLocSet()
        }
    }

    //Get location setting
    private suspend fun getLocSet(): UserOps? {
        return database.getLocOp()
    }

    //Update loc setting
    fun onLocUpdate(item: UserOps, value: Boolean) {
        item.value = value
        viewModelScope.launch {
            updateLoc(item)
        }
    }

    //Update loc
    private suspend fun updateLoc(item: UserOps) {
        database.updateUserOp(item)
    }

    //Create location setting
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