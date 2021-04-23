package com.sid1804492.bottomnavtest.ui.today_tabs

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.Event
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        val moreButton: ImageButton = itemView.findViewById(R.id.today_list_item_more_button)
        private val db = TeacherPlannerDatabase.getInstance(itemView.context)
        val res = itemView.context.resources

        fun bind(item: TeacherPlannerDao.CHomework) {
            setName.text = item.setName
            todoInfo.text = item.todoText

            if(item.complete) {
                setName.apply {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                todoInfo.apply {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                itemView.findViewById<CardView>(R.id.item_card).setCardBackgroundColor(res.getColor(R.color.inactive))
                moreButton.setBackgroundColor(res.getColor(R.color.inactive))

                moreButton.setOnClickListener { view ->
                    val popup: PopupMenu = PopupMenu(view.context, view)
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.today_item_incomplete -> {
                                GlobalScope.launch {
                                    incompleteTodo(item.id)
                                }
                                setName.apply {
                                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                }
                                todoInfo.apply {
                                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                }
                                true
                            }
                            R.id.today_item_delete_two -> {
                                true
                            }
                            R.id.today_item_edit_two -> {
                                true
                            }
                            else -> {
                                false
                            }
                        }
                    }
                    popup.inflate(R.menu.today_item_menu_complete)
                    popup.show()
                }

            } else {

                setName.paintFlags = 0
                todoInfo.paintFlags = 0
                itemView.findViewById<CardView>(R.id.item_card).setCardBackgroundColor(res.getColor(R.color.white))
                moreButton.setBackgroundColor(res.getColor(R.color.white))

                moreButton.setOnClickListener { view ->
                    val popup: PopupMenu = PopupMenu(view.context, view)
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.today_item_complete -> {
                                GlobalScope.launch {
                                    completeTodo(item.id)
                                }
                                true
                            }
                            R.id.today_item_delete -> {
                                true
                            }
                            R.id.today_item_edit -> {
                                true
                            }
                            else -> {
                                false
                            }
                        }
                    }
                    popup.inflate(R.menu.today_item_menu)
                    popup.show()
                }
            }
        }

        private suspend fun completeTodo(id: Long) {
            db.teacherPlannerDao.completeTodo(id)
        }

        private suspend fun incompleteTodo(id: Long) {
            db.teacherPlannerDao.incompleteTodo(id)
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