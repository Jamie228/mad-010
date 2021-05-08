package com.sid1804492.bottomnavtest.ui.classes

import android.app.AlertDialog
import android.content.DialogInterface
import android.opengl.Visibility
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.SchoolClass
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.databinding.FragmentViewClassBinding
import com.sid1804492.bottomnavtest.ui.todo.TodoAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewClassFragment : Fragment() {

    //Lateinit binding and arguments vars so accessible in all methods.
    private lateinit var binding: FragmentViewClassBinding
    private lateinit var arguments: ViewClassFragmentArgs
    private lateinit var curclass: SchoolClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_view_class, container, false
        )

        val application = requireNotNull(this.activity).application
        arguments = ViewClassFragmentArgs.fromBundle(requireArguments())

        val dataSource = TeacherPlannerDatabase.getInstance(application).teacherPlannerDao
        val viewModelFactory = ViewClassViewModelFactory(arguments.classId, dataSource)

        val viewClassViewModel = ViewModelProvider(
            this, viewModelFactory
        ).get(ViewClassViewModel::class.java)

        binding.viewClassViewModel = viewClassViewModel
        binding.lifecycleOwner = this

        viewClassViewModel.onOldDelete()

        //Populate class details
        viewClassViewModel.curClass.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                curclass = it
                binding.viewClassSetName.text = it.SetName
                binding.viewClassSubjectName.text = it.SubjectName + " - " + it.Room
            }
        })

        val adapter = TodoAdapter()
        binding.todoListView.adapter = adapter

        viewClassViewModel.todos.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
                if (adapter.data.size == 0) {
                    binding.noTodoList.visibility = View.VISIBLE
                }
            }
        })

        binding.newTodoFAB.setOnClickListener { view ->
            view.findNavController().navigate(
                ViewClassFragmentDirections.actionNavigationViewClassToNavigationNewTodo(curclass.ClassId)
            )
        }

        // Inflate the layout for this fragment
        return binding.root
    }
}