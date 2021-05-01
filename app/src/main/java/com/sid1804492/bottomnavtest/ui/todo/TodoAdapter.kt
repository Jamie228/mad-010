package com.sid1804492.bottomnavtest.ui.todo

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.database.ToDo
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
        val backg: ConstraintLayout = itemView.findViewById(R.id.todo_list_item_cl)

        fun bind(item: ToDo) {
            val res = itemView.context.resources

            val appDb = TeacherPlannerDatabase.getInstance(itemView.context)

            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val ds = sdf.format(item.TodoDate.timeInMillis)

            todoType.text = item.TodoType + " - " + ds
            todoInfo.text = item.TodoText

            if (item.TodoComplete) {
                todoType.apply {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                todoInfo.apply {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                optionsButton.setBackgroundColor(itemView.resources.getColor(R.color.inactive))
                backg.setBackgroundColor(itemView.resources.getColor(R.color.inactive))

                optionsButton.setOnClickListener { view ->
                    val popup: PopupMenu = PopupMenu(view.context, view)
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.today_item_incomplete -> {
                                GlobalScope.launch {
                                    incompleteTodo(appDb, item.TodoId)
                                }
                                todoType.apply {
                                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                }
                                todoInfo.apply {
                                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                }
                                true
                            }
                            R.id.today_item_delete_two -> {
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
                            R.id.today_item_edit_two ->
                                true
                        }
                        true
                    }
                    popup.inflate(R.menu.today_item_menu_complete)
                    popup.show()
                }
            } else {
                todoInfo.paintFlags = 0
                todoType.paintFlags = 0
                backg.setBackgroundColor(itemView.resources.getColor(R.color.white))
                optionsButton.setBackgroundColor(itemView.resources.getColor(R.color.white))

                optionsButton.setOnClickListener { view ->
                    val popup: PopupMenu = PopupMenu(view.context, view)
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.today_item_complete -> {
                                GlobalScope.launch {
                                    completeTodo(appDb, item.TodoId)
                                }
                                todoType.apply {
                                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                }
                                todoInfo.apply {
                                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                }
                                true
                            }
                            R.id.today_item_delete -> {
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
                                builder.setMessage("Do you want to delete this To-Do?")
                                    .setTitle("Delete To-Do")
                                builder.show()

                                true
                            }
                            R.id.today_item_edit ->
                                true
                        }
                        true
                    }
                    popup.inflate(R.menu.today_item_menu)
                    popup.show()
                }
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

        private suspend fun incompleteTodo(db:TeacherPlannerDatabase, id: Long) {
            db.teacherPlannerDao.incompleteTodo(id)
        }

        private  suspend fun completeTodo(db: TeacherPlannerDatabase, id: Long) {
            db.teacherPlannerDao.completeTodo(id)
        }

    }

}