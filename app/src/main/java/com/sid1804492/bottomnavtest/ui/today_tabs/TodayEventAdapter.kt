package com.sid1804492.bottomnavtest.ui.today_tabs

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.Event
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.ui.today.TodayFragmentDirections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodayEventAdapter : RecyclerView.Adapter<TodayEventAdapter.ViewHolder>() {

    var data = listOf<Event>()
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

        fun bind(item: Event) {
            setName.text = item.EventName
            todoInfo.text = item.EventText
            if (item.Complete) {
                setName.apply {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                todoInfo.apply {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                itemView.findViewById<CardView>(R.id.item_card)
                    .setCardBackgroundColor(res.getColor(R.color.inactive))
                moreButton.setBackgroundColor(res.getColor(R.color.inactive))

                moreButton.setOnClickListener { view ->
                    val popup: PopupMenu = PopupMenu(view.context, view)
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.today_item_incomplete -> {
                                GlobalScope.launch {
                                    incompleteEvent(item.EventId)
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
                                val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                                builder.apply {
                                    setPositiveButton("Delete",
                                        DialogInterface.OnClickListener { dialog, which ->
                                            GlobalScope.launch {
                                                delEvent(item.EventId)
                                            }
                                        })
                                    setNegativeButton("Cancel",
                                        DialogInterface.OnClickListener { dialog, which ->
                                            //Cancel!
                                        })
                                }
                                builder.setMessage("Do you want to delete this Event? This cannot be undone.")
                                    .setTitle("Delete Event?")
                                builder.show()
                                true
                            }
                            R.id.today_item_edit_two -> {
                                view.findNavController().navigate(
                                    TodayFragmentDirections.ActionNavigationHomeToNavigationEditEvent(
                                        item.EventId,
                                        "today"
                                    )
                                )
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
                itemView.findViewById<CardView>(R.id.item_card)
                    .setCardBackgroundColor(res.getColor(R.color.white))
                moreButton.setBackgroundColor(res.getColor(R.color.white))

                moreButton.setOnClickListener { view ->
                    val popup: PopupMenu = PopupMenu(view.context, view)
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.today_item_complete -> {
                                GlobalScope.launch {
                                    completeEvent(item.EventId)
                                }
                                true
                            }
                            R.id.today_item_delete -> {
                                val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                                builder.apply {
                                    setPositiveButton("Delete",
                                        DialogInterface.OnClickListener { dialog, which ->
                                            GlobalScope.launch {
                                                delEvent(item.EventId)
                                            }
                                        })
                                    setNegativeButton("Cancel",
                                        DialogInterface.OnClickListener { dialog, which ->
                                            //Cancel!
                                        })
                                }
                                builder.setMessage("Do you want to delete this Event? This cannot be undone.")
                                    .setTitle("Delete Event?")
                                builder.show()
                                true
                            }
                            R.id.today_item_edit -> {
                                view.findNavController().navigate(
                                    TodayFragmentDirections.ActionNavigationHomeToNavigationEditEvent(
                                        item.EventId,
                                        "today"
                                    )
                                )
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

        private suspend fun delEvent(id: Long) {
            db.teacherPlannerDao.deleteEventWithId(id)
        }

        private suspend fun completeEvent(id: Long) {
            db.teacherPlannerDao.completeEvent(id)
        }

        private suspend fun incompleteEvent(id: Long) {
            db.teacherPlannerDao.incompleteEvent(id)
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