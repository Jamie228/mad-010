package com.sid1804492.bottomnavtest.ui.events

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [NewEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewEventFragment : Fragment() {

    private lateinit var newEventViewModel: NewEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentNewEventBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_event, container, false)

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

        val editTextList = listOf<EditText>(binding.eventName, binding.eventText)

        binding.eventText.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        binding.eventText.isSingleLine = false

        binding.saveNewEventButton.setOnClickListener { view ->
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
            var emptyField: Boolean = false

            for (field in editTextList) {
                if (field.text.toString().trim().isEmpty()) {
                    emptyField = true
                }
            }

            if (!emptyField) {
                val calendar: Calendar = Calendar.getInstance()
                calendar.clear()
                calendar.set(binding.eventDatePicker.year, binding.eventDatePicker.month, binding.eventDatePicker.dayOfMonth)

                newEventViewModel.onSave(
                    binding.eventName.text.toString(),
                    calendar,
                    binding.eventText.text.toString()
                )

                Snackbar.make(view, "Event Saved", Snackbar.LENGTH_SHORT).show()
                view.findNavController().navigate(R.id.action_navigation_new_event_to_navigation_events)

            } else {
                Snackbar.make(view, "Please complete all fields", Snackbar.LENGTH_SHORT).show()
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}