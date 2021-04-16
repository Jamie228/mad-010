package com.sid1804492.bottomnavtest.ui.todo

import android.content.Context
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentEventsBinding
import com.sid1804492.bottomnavtest.databinding.FragmentNewTodoBinding
import com.sid1804492.bottomnavtest.ui.events.EventsViewModel
import com.sid1804492.bottomnavtest.ui.events.EventsViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class NewTodoFragment : Fragment() {

    private lateinit var newTodoViewModel: NewTodoViewModel
    private lateinit var arguments: NewTodoFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentNewTodoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_todo, container, false)

        val application = requireNotNull(this.activity).application
        arguments = NewTodoFragmentArgs.fromBundle(requireArguments())

        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = NewTodoViewModelFactory(dataSource, arguments.classId)

        newTodoViewModel =
            ViewModelProvider(this, viewModelFactory).get(NewTodoViewModel::class.java)

        binding.newTodoViewModel = newTodoViewModel

        binding.todoTextField.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        binding.todoTextField.isSingleLine = false

        val spinner: Spinner = binding.todoTypeSpinner
        ArrayAdapter.createFromResource(this.requireContext(),
        R.array.note_type_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        newTodoViewModel.dateInfo.observe(viewLifecycleOwner, Observer { it ->
            binding.todoDatePicker.updateDate(it["year"]!!, it["month"]!!, it["day"]!!)
        })

        binding.saveTodoButton.setOnClickListener { view ->

            //TODO("Check for empty fields")

            val cal: Calendar = Calendar.getInstance()
            cal.clear()
            cal.set(
                binding.todoDatePicker.year,
                binding.todoDatePicker.month,
                binding.todoDatePicker.dayOfMonth
            )

            newTodoViewModel.onSave(
                spinner.selectedItem.toString(),
                cal,
                binding.todoTextField.text.toString()
            )

            Snackbar.make(
                view,
                "To-Do Saved",
                Snackbar.LENGTH_SHORT
            ).show()

            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)

            view.findNavController().navigate(NewTodoFragmentDirections.actionNavigationNewTodoToNavigationViewClass(arguments.classId))
        }

        binding.lifecycleOwner = this

        return binding.root

    }

}