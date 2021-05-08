package com.sid1804492.bottomnavtest.ui.today_tabs

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
import com.sid1804492.bottomnavtest.databinding.FragmentTodayHomeworkBinding

class TodayHomeworkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTodayHomeworkBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_today_homework, container, false)

        val application = requireActivity().application
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = TodayHomeworkViewModelFactory(dataSource, application)

        val todayHomeworkViewModel =
            ViewModelProvider(this, viewModelFactory).get(TodayHomeworkViewModel::class.java)

        binding.todayHomeworkViewModel = todayHomeworkViewModel
        binding.lifecycleOwner = this

        val adapter = TodayHomeworkAdapter()
        binding.homeworkListRecycler.adapter = adapter

        todayHomeworkViewModel.homeworks.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
                if (adapter.data.size == 0) {
                    binding.noHomeworkText.visibility = View.VISIBLE
                } else {
                    binding.noHomeworkText.visibility = View.GONE
                }
            }
        })

        return binding.root

    }

}