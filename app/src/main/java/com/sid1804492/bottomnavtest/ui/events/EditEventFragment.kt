package com.sid1804492.bottomnavtest.ui.events

import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.Event
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentEditEventBinding
import com.sid1804492.bottomnavtest.emptyFields
import com.sid1804492.bottomnavtest.hideKeyboard
import java.util.*

class EditEventFragment : Fragment() {

    private lateinit var binding: FragmentEditEventBinding
    private lateinit var event: Event
    private lateinit var editEventViewModel: EditEventViewModel
    private lateinit var arguments: EditEventFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_event, container, false
        )
        val application = requireActivity().application
        val dataSource: TeacherPlannerDao =
            TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        arguments = EditEventFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = EditEventViewModelFactory(arguments.eventId, dataSource)
        editEventViewModel =
            ViewModelProvider(this, viewModelFactory).get(EditEventViewModel::class.java)

        binding.editEventViewModel = editEventViewModel
        binding.lifecycleOwner = this

        binding.editEventName.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        binding.editEventName.isSingleLine = true
        binding.editEventText.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        binding.editEventText.isSingleLine = false

        editEventViewModel.event.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.editEventDatePicker.updateDate(
                    it.EventDate.get(Calendar.YEAR),
                    it.EventDate.get(Calendar.MONTH),
                    it.EventDate.get(Calendar.DAY_OF_MONTH)
                )
                binding.editEventName.setText(it.EventName)
                binding.editEventText.setText(it.EventText)
                event = it
            }
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.save_menu_button -> {
            if (!emptyFields(listOf(binding.editEventName, binding.editEventText))) {
                hideKeyboard(requireActivity())
                event.EventText = binding.editEventText.text.toString()
                event.EventName = binding.editEventName.text.toString()
                val c = Calendar.getInstance()
                c.clear()
                c.set(
                    binding.editEventDatePicker.year,
                    binding.editEventDatePicker.month,
                    binding.editEventDatePicker.dayOfMonth
                )
                event.EventDate = c
                editEventViewModel.onUpdate(event)
                Snackbar.make(
                    requireView(),
                    binding.editEventName.text.toString() + " Updated",
                    Snackbar.LENGTH_SHORT
                ).show()
                if (arguments.referrer == "event") {
                    requireView().findNavController().navigate(
                        R.id.action_navigation_edit_event_to_navigation_events
                    )
                } else {
                    requireView().findNavController().navigate(
                        R.id.action_navigation_edit_event_to_navigation_home
                    )
                }
            } else {
                Snackbar.make(
                    requireView(),
                    "Fields Cannot Be Blank",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            true
        }
        else -> {
            false
        }
    }

}