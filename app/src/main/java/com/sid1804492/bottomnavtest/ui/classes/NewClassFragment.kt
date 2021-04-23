package com.sid1804492.bottomnavtest.ui.classes

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentNewClassBinding
import com.sid1804492.bottomnavtest.hideKeyboard

class NewClassFragment : Fragment() {

    private lateinit var newClassViewModel: NewClassViewModel
    private lateinit var binding: FragmentNewClassBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_class, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = NewClassViewModelFactory(dataSource, application)

        newClassViewModel = ViewModelProvider(this, viewModelFactory).get(NewClassViewModel::class.java)

        binding.newClassViewModel = newClassViewModel
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
            hideKeyboard(requireActivity())
            newClassViewModel.onSave(
                name = binding.subjectName.text.toString(),
                room = binding.classroom.text.toString(),
                set = binding.setName.text.toString(),
                year = binding.yearGroup.text.toString()
            )
            Snackbar.make(
                requireView(),
                "Class Saved",
                Snackbar.LENGTH_SHORT
            ).show()
            view?.findNavController()?.navigate(R.id.action_navigation_new_class_to_navigation_classes)
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