package com.sid1804492.bottomnavtest.ui.todo

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
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
import com.sid1804492.bottomnavtest.hideKeyboard
import com.sid1804492.bottomnavtest.ui.events.EventsViewModel
import com.sid1804492.bottomnavtest.ui.events.EventsViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class NewTodoFragment : Fragment() {

    private lateinit var newTodoViewModel: NewTodoViewModel
    private lateinit var arguments: NewTodoFragmentArgs
    private lateinit var binding: FragmentNewTodoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
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

        binding.lifecycleOwner = this

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
            val cal: Calendar = Calendar.getInstance()
            cal.clear()
            cal.set(
                binding.todoDatePicker.year,
                binding.todoDatePicker.month,
                binding.todoDatePicker.dayOfMonth
            )
            newTodoViewModel.onSave(
                binding.todoTypeSpinner.selectedItem.toString(),
                cal,
                binding.todoTextField.text.toString()
            )
            view?.let {
                Snackbar.make(
                    it,
                    "To-Do Saved",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
//            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
            hideKeyboard(requireActivity())
            view?.findNavController()?.navigate(NewTodoFragmentDirections.actionNavigationNewTodoToNavigationViewClass(arguments.classId))

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