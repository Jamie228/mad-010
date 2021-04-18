package com.sid1804492.bottomnavtest.ui.options

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentOptionsBinding

class OptionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentOptionsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_options, container, false)

        val application = requireActivity().application
        val datasource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = OptionsViewModelFactory(datasource, application)

        val optionsViewModel =
            ViewModelProvider(this, viewModelFactory).get(OptionsViewModel::class.java)

        binding.optionsViewModel = optionsViewModel
        binding.lifecycleOwner = this

        optionsViewModel.location.observe(viewLifecycleOwner, Observer { it ->
            if (it != null) {
                binding.LocationOnSwitch.isChecked = it.value
            } else {
                binding.LocationOnSwitch.isChecked = false
            }
        })

        binding.LocationOnSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            optionsViewModel.onLocationChange(isChecked)
        }

        return binding.root
    }

}