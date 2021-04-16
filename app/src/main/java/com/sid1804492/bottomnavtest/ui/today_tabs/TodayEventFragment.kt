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
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentTodayEventBinding

class TodayEventFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTodayEventBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_today_event, container, false)

        val application = requireActivity().application
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = TodayEventViewModelFactory(dataSource, application)

        val todayEventViewModel =
            ViewModelProvider(this, viewModelFactory).get(TodayEventViewModel::class.java)

        binding.todayEventViewModel = todayEventViewModel
        binding.lifecycleOwner = this

        val adapter = TodayEventAdapter()
        binding.eventsRecyclerView.adapter = adapter

        todayEventViewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.data = it
                if(adapter.data.size == 0) {
                    binding.noEventsListText.visibility = View.VISIBLE
                } else {
                    binding.noEventsListText.visibility = View.GONE
                }
            }
        })

        return binding.root

    }

}