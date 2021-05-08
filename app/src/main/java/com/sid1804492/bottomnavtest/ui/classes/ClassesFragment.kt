package com.sid1804492.bottomnavtest.ui.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentClassesBinding

class ClassesFragment : Fragment() {

    private lateinit var classesViewModel: ClassesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentClassesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_classes, container, false
        )

        val application = requireActivity().application
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = ClassesViewModelFactory(dataSource, application)

        classesViewModel =
            ViewModelProvider(this, viewModelFactory).get(ClassesViewModel::class.java)

        binding.classesViewModel = classesViewModel
        binding.lifecycleOwner = this

        binding.newClassButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_navigation_classes_to_navigation_new_class)
        }

        val adapter = SchoolClassAdapter()
        binding.classList.adapter = adapter

        classesViewModel.classes.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
                if (adapter.data.size == 0) {
                    binding.noClassesText.visibility = View.VISIBLE
                } else {
                    binding.noClassesText.visibility = View.GONE
                }
            }
        })

        return binding.root
    }
}