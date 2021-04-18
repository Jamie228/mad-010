package com.sid1804492.bottomnavtest.ui.options

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.UserOps
import kotlinx.coroutines.launch

class OptionsViewModel(val database: TeacherPlannerDao, application: Application): AndroidViewModel(application) {

    val location = MutableLiveData<UserOps?>()

    init {
        initLocation()
    }

    private fun initLocation() {
        viewModelScope.launch {
            location.value = getLocation()
        }
    }

    private suspend fun getLocation() : UserOps? {
        return database.getLocOp()
    }

    fun onLocationChange(userChoice: Boolean) {
        if(location.value != null && location.value!!.value != userChoice) {
            viewModelScope.launch {
                updateLoc(userChoice)
            }
        } else if (location.value == null) {
            viewModelScope.launch {
                createLoc(userChoice)
            }
        }
    }

    private suspend fun updateLoc(value: Boolean) {
        location.value!!.value = value
        database.updateUserOp(location.value!!)
    }

//    fun onLocationOn() {
//        if(!location.value?.value!!) {
//            location.value!!.value = true
//            viewModelScope.launch {
//                locationOn(location.value!!)
//            }
//        } else if (location.value == null) {
//            viewModelScope.launch() {
//                createLoc(true)
//            }
//        }
//    }

//    private suspend fun locationOn(loc: UserOps) {
//        database.updateUserOp(loc)
//    }

    private suspend fun createLoc(value: Boolean) {
        val loc = UserOps("LOCATION", value)
        database.insertUserOp(loc)
    }

}