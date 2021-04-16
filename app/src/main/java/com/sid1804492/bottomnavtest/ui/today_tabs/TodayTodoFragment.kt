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
import com.sid1804492.bottomnavtest.databinding.FragmentTodayTodoBinding
import com.sid1804492.bottomnavtest.ui.events.EventsViewModelFactory

class TodayTodoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentTodayTodoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_today_todo, container, false)

        val application = requireActivity().application
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = TodayTodoViewModelFactory(dataSource, application)

        val todayTodoViewModel =
            ViewModelProvider(this, viewModelFactory).get(TodayTodoViewModel::class.java)

        binding.todayTodoViewModel = todayTodoViewModel
        binding.lifecycleOwner = this

        val adapter = TodayTodoAdapter()
        binding.todoList.adapter = adapter

        todayTodoViewModel.todos.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.data = it
                if (adapter.data.size == 0) {
                    binding.noTodayTodoText.visibility = View.VISIBLE
                } else {
                    binding.noTodayTodoText.visibility = View.GONE
                }
            }
        })

        return binding.root

    }

}