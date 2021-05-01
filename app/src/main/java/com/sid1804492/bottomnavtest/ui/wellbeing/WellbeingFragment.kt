package com.sid1804492.bottomnavtest.ui.wellbeing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentWellbeingBinding
import kotlin.math.roundToInt

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

        binding.addWellbeingButton.setOnClickListener {
            requireView().findNavController().navigate(R.id.action_navigation_wellbeing_to_navigation_new_wellbeing)
        }

        val adapter = WellbeingAdapter()
        binding.recyclerView.adapter = adapter

        wellbeingViewModel.wellbeings.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
                if (adapter.data.size == 0) {
                    binding.noWellbeingText.visibility = View.VISIBLE
                } else {
                    binding.noWellbeingText.visibility = View.GONE
                    var total = 0.0f
                    for (x in it) {
                        total += x.rating
                    }
                    val avg = (total/it.size).roundToInt()
                    binding.averageText.text = avg.toString()
                    when(avg) {
                        1 -> {
                          binding.averageText.setTextColor(resources.getColor(R.color.red))
                        }
                        2 -> {
                            binding.averageText.setTextColor(resources.getColor(R.color.orange))
                        }
                        3 -> {
                            binding.averageText.setTextColor(resources.getColor(R.color.amber))
                        }
                        4 -> {
                            binding.averageText.setTextColor(resources.getColor(R.color.yellow))
                        }
                        5 -> {
                            binding.averageText.setTextColor(resources.getColor(R.color.green))
                        }
                    }
                }
            }
        })

        return binding.root

    }

}