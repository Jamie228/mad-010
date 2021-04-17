package com.sid1804492.bottomnavtest

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        imm.hideSoftInputFromWindow(
            currentFocusedView.windowToken, 0
        )
    }
}