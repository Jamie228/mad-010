package com.sid1804492.bottomnavtest.ui.wellbeing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentWellbeingBinding

class WellbeingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentWellbeingBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_wellbeing, container, false
        )

        val application = requireActivity().application
        val datasource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = WellbeingViewModelFactory(datasource, application)
        val wellbeingViewModel = ViewModelProvider(this, viewModelFactory).get(WellbeingViewModel::class.java)

        binding.wellbeingViewModel = wellbeingViewModel
        binding.lifecycleOwner = this

        return binding.root

    }

}