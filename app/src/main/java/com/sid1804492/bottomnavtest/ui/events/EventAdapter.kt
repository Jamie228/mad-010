package com.sid1804492.bottomnavtest.ui.classes

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sid1804492.bottomnavtest.MainActivity
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

        fun bind(item: Event) {
            val res = itemView.context.resources

            val appDb = TeacherPlannerDatabase.getInstance(itemView.context)

            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val ds = sdf.format(item.EventDate.timeInMillis)

            eventMoreDetails.text = item.EventText
            eventDetails.text = item.EventName + " - " + ds
            editButton.setOnClickListener { view ->
                val popup: PopupMenu = PopupMenu(view.context, view)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.delete_event_menu_item -> {
                            //
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
                            builder.setMessage("Do you want to delete the event: " + item.EventName + "?").setTitle("Delete Event")
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
                    .inflate(R.layout.event_list_item, parent, false)

                return ViewHolder(view)
            }
        }

        private suspend fun delEv(db: TeacherPlannerDatabase, event: Event) {
            db.teacherPlannerDao.deleteEvent(event)
        }

    }

}