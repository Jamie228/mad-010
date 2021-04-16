package com.sid1804492.bottomnavtest.ui.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentEventsBinding
import com.sid1804492.bottomnavtest.ui.classes.EventAdapter
import com.sid1804492.bottomnavtest.ui.classes.SchoolClassAdapter

class EventsFragment : Fragment() {

    private lateinit var eventsViewModel: EventsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentEventsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_events, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = EventsViewModelFactory(dataSource, application)

        eventsViewModel =
                ViewModelProvider(this, viewModelFactory).get(EventsViewModel::class.java)

        binding.eventsViewModel = eventsViewModel
        binding.lifecycleOwner = this

        binding.newEventButton.setOnClickListener{ view: View ->
            view.findNavController().navigate(R.id.action_navigation_notifications_to_fragment_new_event)
        }

        val adapter = EventAdapter()
        binding.eventList.adapter = adapter

        eventsViewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
                if(adapter.data.size == 0) {
                    binding.noEventsText.visibility = View.VISIBLE
                } else {
                    binding.noEventsText.visibility = View.GONE
                }
            }
        })

        return binding.root
    }
}