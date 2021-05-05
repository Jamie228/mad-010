package com.sid1804492.bottomnavtest.ui.classes

import android.os.Bundle
import android.text.InputType
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.SchoolClass
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentEditClassBinding
import com.sid1804492.bottomnavtest.emptyFields
import com.sid1804492.bottomnavtest.hideKeyboard

class EditClassFragment : Fragment() {

    private lateinit var binding: FragmentEditClassBinding
    private lateinit var arguments: EditClassFragmentArgs
    private lateinit var curClass: SchoolClass
    private lateinit var editClassViewModel: EditClassViewModel
    private lateinit var inputs: List<EditText>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_class, container, false
        )
        val application = requireActivity().application
        arguments = EditClassFragmentArgs.fromBundle(requireArguments())
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = EditClassViewModelFactory(arguments.classId, dataSource)
        editClassViewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditClassViewModel::class.java)

        binding.editClassViewModel = editClassViewModel
        binding.lifecycleOwner = this

        inputs = listOf(binding.editClassroom, binding.editSetName, binding.editSubjectName, binding.editYearGroup)
        for (f in inputs) {
            f.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
            f.isSingleLine = true
        }

        editClassViewModel.curClass.observe(viewLifecycleOwner, Observer { sc ->
            binding.editClassroom.setText(sc.Room)
            binding.editSetName.setText(sc.SetName)
            binding.editSubjectName.setText(sc.SubjectName)
            binding.editYearGroup.setText(sc.YearGroup)
            if (sc != null) {
                curClass = sc
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when (item.itemId) {
        R.id.save_menu_button -> {
            if (!emptyFields(inputs)) {
                curClass.YearGroup = binding.editYearGroup.text.toString()
                curClass.SubjectName = binding.editSubjectName.text.toString()
                curClass.SetName = binding.editSetName.text.toString()
                curClass.Room = binding.editClassroom.text.toString()
                hideKeyboard(requireActivity())
                editClassViewModel.onUpdate(curClass)
                Snackbar.make(requireView(), binding.editSetName.text.toString() + " Updated", Snackbar.LENGTH_SHORT).show()
                requireView().findNavController().navigate(R.id.action_navigation_edit_class_to_navigation_classes)
            } else {
                Snackbar.make(requireView(),
                "Fields Cannot Be Empty",
                Snackbar.LENGTH_SHORT).show()
            }
            true
        }
        else -> {
            false
        }
    }

}