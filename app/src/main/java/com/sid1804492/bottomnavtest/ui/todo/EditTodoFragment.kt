package com.sid1804492.bottomnavtest.ui.todo

import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.database.ToDo
import com.sid1804492.bottomnavtest.databinding.FragmentEditTodoBinding
import com.sid1804492.bottomnavtest.emptyFields
import com.sid1804492.bottomnavtest.hideKeyboard
import java.util.*

class EditTodoFragment : Fragment() {

    private lateinit var binding: FragmentEditTodoBinding
    private lateinit var arguments: EditTodoFragmentArgs
    private lateinit var td: ToDo
    private lateinit var editTodoViewModel: EditTodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_todo, container, false
        )
        val application = requireActivity().application
        arguments = EditTodoFragmentArgs.fromBundle(requireArguments())
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = EditTodoViewModelFactory(arguments.todoId, dataSource)
        editTodoViewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditTodoViewModel::class.java)

        binding.editTodoViewModel = editTodoViewModel
        binding.lifecycleOwner = this

        val spinner: Spinner = binding.editTodoTypeSpinner
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.note_type_array, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        binding.editTodoTextField.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        binding.editTodoTextField.isSingleLine = false

        editTodoViewModel.todo.observe(viewLifecycleOwner, Observer { todo ->
            todo?.let {
                binding.editTodoTextField.setText(todo.TodoText)
                binding.editTodoDatePicker.updateDate(
                    todo.TodoDate.get(Calendar.YEAR),
                    todo.TodoDate.get(Calendar.MONTH),
                    todo.TodoDate.get(Calendar.DAY_OF_MONTH)
                )
                if (todo.TodoType == "To-Do") {
                    spinner.setSelection(0)
                } else if (todo.TodoType == "Homework") {
                    spinner.setSelection(1)
                }
                td = todo
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
            if (!emptyFields(listOf(binding.editTodoTextField))) {
                hideKeyboard(requireActivity())
                td.TodoText = binding.editTodoTextField.text.toString()
                val c = Calendar.getInstance()
                c.clear()
                c.set(
                    binding.editTodoDatePicker.year,
                    binding.editTodoDatePicker.month,
                    binding.editTodoDatePicker.dayOfMonth
                )
                td.TodoDate = c
                td.TodoType = binding.editTodoTypeSpinner.selectedItem.toString()
                editTodoViewModel.onUpdate(td)
                Snackbar.make(
                    requireView(),
                    td.TodoType + " Updated",
                    Snackbar.LENGTH_SHORT
                ).show()
                if (arguments.referrer == "class") {
                    requireView().findNavController().navigate(
                        EditTodoFragmentDirections.actionNavigationEditTodoToNavigationViewClass(td.ClassId)
                    )
                } else {
                    requireView().findNavController().navigate(R.id.action_navigation_edit_todo_to_navigation_home)
                }
            } else {
                Snackbar.make(
                    requireView(),
                    "Fields Cannot be Blank",
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