package com.sid1804492.bottomnavtest.ui.today_tabs

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.database.ToDo
import com.sid1804492.bottomnavtest.ui.classes.EventAdapter
import kotlinx.android.synthetic.main.today_todo_item.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodayTodoAdapter : RecyclerView.Adapter<TodayTodoAdapter.ViewHolder>() {

    var data = listOf<TeacherPlannerDao.CTodo>()
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
        val setName: TextView = itemView.findViewById(R.id.today_todo_set)
        val todoInfo: TextView = itemView.findViewById(R.id.today_todo_info)

        val appDb = TeacherPlannerDatabase.getInstance(itemView.context)

        fun bind(item: TeacherPlannerDao.CTodo) {
            setName.text = item.setName
            todoInfo.text = item.todoText
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.today_todo_item, parent, false)
                return ViewHolder(view)
            }
        }
    }

}