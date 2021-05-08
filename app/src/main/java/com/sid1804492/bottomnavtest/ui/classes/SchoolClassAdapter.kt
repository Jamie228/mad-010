package com.sid1804492.bottomnavtest.ui.classes

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.SchoolClass
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.hideKeyboard
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SchoolClassAdapter : RecyclerView.Adapter<SchoolClassAdapter.ViewHolder>() {

    var data = listOf<SchoolClass>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val classDetails: TextView = itemView.findViewById(R.id.classListText)
        val menuButton: ImageButton = itemView.findViewById(R.id.classMenuButton)

        fun bind(item: SchoolClass) {
            val res = itemView.context.resources
            val appDb = TeacherPlannerDatabase.getInstance(itemView.context)
            classDetails.text = item.SetName + " - " + item.SubjectName + " - " + item.Room
            classDetails.setOnClickListener { view ->
                view.findNavController().navigate(
                    ClassesFragmentDirections.actionNavigationClassesToNavigationViewClass(item.ClassId)
                )
            }

            menuButton.setOnClickListener { view ->
                val popup: PopupMenu = PopupMenu(view.context, view)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.delete_menu_item -> {
                            val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                            builder.apply {
                                setPositiveButton("Delete",
                                    DialogInterface.OnClickListener { dialog, id ->
                                        GlobalScope.launch {
                                            delClass(item, appDb)
                                        }
                                    }
                                )
                                setNegativeButton("Cancel",
                                    DialogInterface.OnClickListener { dialog, id ->
                                        //Cancel!
                                    })
                            }
                            builder.setMessage("Do you want to delete " + item.SetName.toString() + "? This will delete all associated To-Do items, and cannot be undone.")
                                .setTitle("Delete Class")
                            builder.show()

                            true
                        }
                        R.id.edit_menu_item -> {
                            view.findNavController().navigate(
                                ClassesFragmentDirections.actionNavigationClassesToNavigationEditClass(
                                    item.ClassId
                                )
                            )
                            true
                        }
                    }
                    true
                }
                popup.inflate(R.menu.edit_event_menu)
                popup.show()
            }

        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val view = layoutInflater
                    .inflate(R.layout.class_list_item, parent, false)

                return ViewHolder(view)
            }
        }

        private suspend fun delClass(sclass: SchoolClass, db: TeacherPlannerDatabase) {
            db.teacherPlannerDao.deleteClass(sclass)
        }
    }

}