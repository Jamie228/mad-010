package com.sid1804492.bottomnavtest.ui.classes

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
import com.sid1804492.bottomnavtest.database.Event
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class EventAdapter : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

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
        val eventDetails: TextView = itemView.findViewById(R.id.eventListText)
        val eventMoreDetails: TextView = itemView.findViewById(R.id.eventDetails)
        val editButton: ImageButton = itemView.findViewById(R.id.editEventMenuButton)
        val cl: ConstraintLayout = itemView.findViewById(R.id.event_list_item_cl)

        fun bind(item: Event) {
            val res = itemView.context.resources

            val appDb = TeacherPlannerDatabase.getInstance(itemView.context)

            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val ds = sdf.format(item.EventDate.timeInMillis)

            eventMoreDetails.text = item.EventText
            eventDetails.text = item.EventName + " - " + ds

            if (item.Complete) {
                eventMoreDetails.apply {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                eventDetails.apply {
                    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                editButton.setBackgroundColor(res.getColor(R.color.inactive))
                cl.setBackgroundColor(res.getColor(R.color.inactive))

                editButton.setOnClickListener { view ->
                    val popup: PopupMenu = PopupMenu(view.context, view)
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.today_item_incomplete -> {
                                GlobalScope.launch {
                                    incompleteEvent(appDb, item.EventId)
                                }
                                eventDetails.apply {
                                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                }
                                eventMoreDetails.apply {
                                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                                }
                            }
                            R.id.today_item_delete_two -> {
                                val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                                builder.apply {
                                    setPositiveButton("Delete",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            GlobalScope.launch {
                                                delEv(appDb, item)
                                            }
                                        }
                                    )
                                    setNegativeButton("Cancel",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            //Cancel!
                                        })
                                }
                                builder.setMessage("Do you want to delete the event: " + item.EventName + "?")
                                    .setTitle("Delete Event")
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
                eventMoreDetails.paintFlags = 0
                eventDetails.paintFlags = 0
                editButton.setBackgroundColor(res.getColor(R.color.white))
                cl.setBackgroundColor(res.getColor(R.color.white))

                editButton.setOnClickListener { view ->
                    val popup: PopupMenu = PopupMenu(view.context, view)
                    popup.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.today_item_complete -> {
                                GlobalScope.launch {
                                    completeEvent(appDb, item.EventId)
                                }
                            }
                            R.id.today_item_delete -> {
                                val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                                builder.apply {
                                    setPositiveButton("Delete",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            GlobalScope.launch {
                                                delEv(appDb, item)
                                            }
                                        }
                                    )
                                    setNegativeButton("Cancel",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            //Cancel!
                                        })
                                }
                                builder.setMessage("Do you want to delete the event: " + item.EventName + "?")
                                    .setTitle("Delete Event")
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
                    .inflate(R.layout.event_list_item, parent, false)

                return ViewHolder(view)
            }
        }

        private suspend fun delEv(db: TeacherPlannerDatabase, event: Event) {
            db.teacherPlannerDao.deleteEvent(event)
        }

        private suspend fun incompleteEvent(db: TeacherPlannerDatabase, id: Long) {
            db.teacherPlannerDao.incompleteEvent(id)
        }

        private suspend fun completeEvent(db: TeacherPlannerDatabase, id: Long) {
            db.teacherPlannerDao.completeEvent(id)
        }

    }

}