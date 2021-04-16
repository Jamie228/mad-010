package com.sid1804492.bottomnavtest.ui.todo

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.Event
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.database.ToDo
import com.sid1804492.bottomnavtest.ui.classes.EventAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    var data = listOf<ToDo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TodoAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.ViewHolder {
        return TodoAdapter.ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoType: TextView = itemView.findViewById(R.id.todoTypeAndDate)
        val todoInfo: TextView = itemView.findViewById(R.id.todoDetails)
        val optionsButton: ImageButton = itemView.findViewById(R.id.todoOptionsButton)

        fun bind(item: ToDo) {
            val res = itemView.context.resources

            val appDb = TeacherPlannerDatabase.getInstance(itemView.context)

            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val ds = sdf.format(item.TodoDate.timeInMillis)

            todoType.text = item.TodoType + " - " + ds
            todoInfo.text = item.TodoText

            optionsButton.setOnClickListener { view ->
                val popup: PopupMenu = PopupMenu(view.context, view)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.delete_event_menu_item -> {
                            val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                            builder.apply {
                                setPositiveButton("Delete",
                                    DialogInterface.OnClickListener { dialog, id ->
                                        GlobalScope.launch {
                                            delTodo(appDb, item)
                                        }
                                    }
                                )
                                setNegativeButton("Cancel",
                                    DialogInterface.OnClickListener { dialog, id ->
                                        //Cancel!
                                    })
                            }
                            builder.setMessage("Do you want to delete this To-Do?").setTitle("Delete To-Do")
                            builder.show()

                            true
                        }
                        R.id.edit_event_menu_item ->
                            true
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
                    .inflate(R.layout.todo_list_item, parent, false)

                return ViewHolder(view)
            }
        }

        private suspend fun delTodo(db: TeacherPlannerDatabase, todo: ToDo) {
            db.teacherPlannerDao.deleteTodo(todo)
        }

    }

}