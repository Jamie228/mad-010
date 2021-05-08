package com.sid1804492.bottomnavtest.ui.today_tabs

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import java.util.*

class TodayTodoViewModel(val database: TeacherPlannerDao, application: Application) :
    AndroidViewModel(application) {

    private lateinit var nCal: Calendar

    init {
        val cal: Calendar = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        nCal = Calendar.getInstance()
        nCal.clear()
        nCal.set(y, m, d)
    }

    val todos = database.getTodayTodos(nCal.timeInMillis)

}