package com.sid1804492.bottomnavtest.ui.wellbeing

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.database.Wellbeing
import com.sid1804492.bottomnavtest.databinding.FragmentEditWellbeingBinding
import com.sid1804492.bottomnavtest.emptyFields
import com.sid1804492.bottomnavtest.hideKeyboard

class EditWellbeingFragment : Fragment() {

    private lateinit var binding: FragmentEditWellbeingBinding
    private lateinit var wellb: Wellbeing
    private lateinit var editWellbeingViewModel: EditWellbeingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_wellbeing, container, false
        )
        val application = requireActivity().application
        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val arguments = EditWellbeingFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = EditWellbeingViewModelFactory(arguments.wbId, dataSource)
        editWellbeingViewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditWellbeingViewModel::class.java)
        binding.editWellbeingViewModel = editWellbeingViewModel
        binding.lifecycleOwner = this

        editWellbeingViewModel.wb.observe(viewLifecycleOwner, Observer {
            binding.editWellbeingRating.rating = it.rating.toFloat()
            binding.editWellbeingGoodText.setText(it.wentWell)
            binding.editWellbeingBadText.setText(it.improveOn)
            binding.editWellbeingMoreText.setText(it.anythingElse)
            wellb = it
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.save_menu_button -> {
            if (!emptyFields(
                    listOf(
                        binding.editWellbeingBadText,
                        binding.editWellbeingGoodText
                    )
                ) && binding.editWellbeingRating.rating != 0.0f
            ) {
                wellb.rating = binding.editWellbeingRating.rating.toInt()
                wellb.anythingElse = binding.editWellbeingMoreText.text.toString()
                wellb.improveOn = binding.editWellbeingBadText.text.toString()
                wellb.wentWell = binding.editWellbeingGoodText.text.toString()
                hideKeyboard(requireActivity())
                editWellbeingViewModel.onUpdate(wellb)
                Snackbar.make(
                    requireView(),
                    "Reflection Updated",
                    Snackbar.LENGTH_SHORT
                ).show()
                requireView().findNavController().navigate(
                    R.id.action_navigation_edit_wellbeing_to_navigation_wellbeing
                )
            } else {
                Snackbar.make(
                    requireView(),
                    "Fields Cannot Be Blank.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            true
        }
        else -> {
            false
        }
    }
}