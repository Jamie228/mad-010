package com.sid1804492.bottomnavtest.ui.today_tabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase

class TodayHomeworkAdapter : RecyclerView.Adapter<TodayHomeworkAdapter.ViewHolder>() {

    var data = listOf<TeacherPlannerDao.CHomework>()
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
        val setName: TextView = itemView.findViewById(R.id.today_card_title)
        val todoInfo: TextView = itemView.findViewById(R.id.today_card_info)

        fun bind(item: TeacherPlannerDao.CHomework) {
            setName.text = item.setName
            todoInfo.text = item.todoText
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.today_list_item, parent, false)
                return ViewHolder(view)
            }
        }
    }

}