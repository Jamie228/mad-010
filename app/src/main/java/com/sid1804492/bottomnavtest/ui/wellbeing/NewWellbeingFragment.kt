package com.sid1804492.bottomnavtest.ui.wellbeing

import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.database.Wellbeing
import com.sid1804492.bottomnavtest.databinding.FragmentNewWellbeingBinding
import com.sid1804492.bottomnavtest.databinding.FragmentWellbeingBinding
import com.sid1804492.bottomnavtest.hideKeyboard
import java.util.*

class NewWellbeingFragment : Fragment() {

    private lateinit var newWellbeingViewModel: NewWellbeingViewModel
    private lateinit var binding: FragmentNewWellbeingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_wellbeing, container, false
        )

        val application = requireActivity().application
        val db = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val vmf = NewWellbeingViewModelFactory(db, application)

        newWellbeingViewModel = ViewModelProvider(
            this, vmf
        ).get(NewWellbeingViewModel::class.java)

        binding.newWellbeingViewModel = newWellbeingViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.save_menu_button -> {

            val textFields = listOf<EditText>(binding.wellbeingBadText, binding.wellbeingGoodText, binding.wellbeingMoreText)
            var emptyField: Boolean = false
            var ratingEmpty: Boolean = false
            hideKeyboard(requireActivity())

            for (f in textFields) {
                if (f.text.toString().trim().isEmpty()) {
                    emptyField = true
                }
            }
            if (binding.wellbeingRating.rating == 0.0f) {
                ratingEmpty = true
            }

            if (!emptyField && !ratingEmpty) {

                val c = Calendar.getInstance()
                val y = c.get(Calendar.YEAR)
                val m = c.get(Calendar.MONTH)
                val d = c.get(Calendar.DAY_OF_MONTH)
                c.clear()
                c.set(y,m,d)

                val newWb = Wellbeing(
                    date = c,
                    rating = binding.wellbeingRating.rating.toInt(),
                    wentWell = binding.wellbeingGoodText.text.toString(),
                    improveOn = binding.wellbeingBadText.text.toString(),
                    anythingElse = binding.wellbeingMoreText.text.toString()
                )
                newWellbeingViewModel.onSave(newWb)
                Snackbar.make(
                    requireView(),
                    "Reflection Saved.",
                    Snackbar.LENGTH_SHORT
                ).show()
                requireView().findNavController().navigate(R.id.action_navigation_new_wellbeing_to_navigation_wellbeing)
            } else {
                Snackbar.make(
                    requireView(),
                    "Fields Cannot be Blank.",
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