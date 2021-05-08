package com.sid1804492.bottomnavtest.ui.events

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentNewEventBinding
import com.sid1804492.bottomnavtest.hideKeyboard
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class NewEventFragment : Fragment() {

    private lateinit var newEventViewModel: NewEventViewModel
    private lateinit var binding: FragmentNewEventBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_event, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = NewEventViewModelFactory(dataSource, application)

        newEventViewModel =
            ViewModelProvider(this, viewModelFactory).get(NewEventViewModel::class.java)

        binding.newEventViewModel = newEventViewModel
        binding.lifecycleOwner = this

        newEventViewModel.dateInfo.observe(viewLifecycleOwner, Observer { date ->
            binding.eventDatePicker.updateDate(date["year"]!!, date["month"]!!, date["day"]!!)
        })

        binding.eventText.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        binding.eventText.isSingleLine = false

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.save_menu_button -> {
            val editTextList = listOf<EditText>(binding.eventName, binding.eventText)
            hideKeyboard(requireActivity())
            var emptyField: Boolean = false

            for (field in editTextList) {
                if (field.text.toString().trim().isEmpty()) {
                    emptyField = true
                }
            }

            if (!emptyField) {
                val calendar: Calendar = Calendar.getInstance()
                calendar.clear()
                calendar.set(
                    binding.eventDatePicker.year,
                    binding.eventDatePicker.month,
                    binding.eventDatePicker.dayOfMonth
                )

                newEventViewModel.onSave(
                    binding.eventName.text.toString(),
                    calendar,
                    binding.eventText.text.toString()
                )

                view?.let { Snackbar.make(it, "Event Saved", Snackbar.LENGTH_SHORT).show() }
                view?.findNavController()
                    ?.navigate(R.id.action_navigation_new_event_to_navigation_events)

            } else {
                view?.let {
                    Snackbar.make(
                        it,
                        "You cannot leave fields blank.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            true
        }
        else -> {
            false
        }
    }

    override fun onDestroy() {
        hideKeyboard(requireActivity())
        super.onDestroy()
    }
}