package com.sid1804492.bottomnavtest

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Hides the keyboard from view if present.
 *
 * @param activity The current Activity
 * @author 1804492 / P110103851
 */
fun hideKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val currentFocusedView = activity.currentFocus
    currentFocusedView?.let {
        imm.hideSoftInputFromWindow(
            currentFocusedView.windowToken, 0
        )
    }
}

/**
 * Checks if any given text fields are blank
 *
 * @param fields The list of EditText
 * @return True if any field is blank. Otherwise false
 * @author 1804492 / P110103851
 */
fun emptyFields(fields: List<EditText>) : Boolean {
    for (f in fields) {
        if (f.text.toString().trim().isEmpty()) {
            return true
        }
    }
    return false
}