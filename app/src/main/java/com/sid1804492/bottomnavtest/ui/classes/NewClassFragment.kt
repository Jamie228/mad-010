package com.sid1804492.bottomnavtest.ui.classes

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

/**
 * A simple [Fragment] subclass.
 * Use the [NewClassFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewClassFragment : Fragment() {

    private lateinit var newClassViewModel: NewClassViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentNewClassBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_class, container, false
        )
        val application = requireNotNull(this.activity).application
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = NewClassViewModelFactory(dataSource, application)

        newClassViewModel = ViewModelProvider(this, viewModelFactory).get(NewClassViewModel::class.java)

        binding.newClassViewModel = newClassViewModel
        binding.lifecycleOwner = this

        binding.saveNewClass.setOnClickListener { view ->
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
            newClassViewModel.onSave(
                name = binding.subjectName.text.toString(),
                room = binding.roomNumber.text.toString(),
                set = binding.setName.text.toString(),
                year = binding.yearGroup.text.toString()
            )
            Snackbar.make(
                view,
                "Class Saved",
                Snackbar.LENGTH_SHORT
            ).show()
            view.findNavController().navigate(R.id.action_navigation_new_class_to_navigation_classes)

        }

        return binding.root
    }
}