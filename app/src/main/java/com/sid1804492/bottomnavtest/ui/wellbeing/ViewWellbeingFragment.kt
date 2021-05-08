package com.sid1804492.bottomnavtest.ui.wellbeing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentViewWellbeingBinding

class ViewWellbeingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentViewWellbeingBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_view_wellbeing, container, false
        )
        val application = requireActivity().application
        val arguments = ViewWellbeingFragmentArgs.fromBundle(requireArguments())
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = ViewWellbeingViewModelFactory(arguments.wbId, dataSource)
        val viewWellbeingViewModel = ViewModelProvider(this, viewModelFactory)
            .get(ViewWellbeingViewModel::class.java)
        binding.viewWellbeingViewModel = viewWellbeingViewModel
        binding.lifecycleOwner = this

        viewWellbeingViewModel.wb.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.score.text = it.rating.toString()
                binding.wwwContent.text = it.wentWell
                binding.ebiContent.text = it.improveOn
                binding.otherContent.text = it.anythingElse
            }
        })

        return binding.root
    }
}