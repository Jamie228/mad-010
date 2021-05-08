package com.sid1804492.bottomnavtest.ui.wellbeing

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color.red
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.TeacherPlannerDao
import com.sid1804492.bottomnavtest.database.TeacherPlannerDatabase
import com.sid1804492.bottomnavtest.database.Wellbeing
import com.sid1804492.bottomnavtest.ui.todo.TodoAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class WellbeingAdapter : RecyclerView.Adapter<WellbeingAdapter.ViewHolder>() {

    var data = listOf<Wellbeing>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: WellbeingAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WellbeingAdapter.ViewHolder {
        return WellbeingAdapter.ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(wb: Wellbeing) {
            val score = itemView.findViewById<TextView>(R.id.wellbeingScore)
            val date = itemView.findViewById<TextView>(R.id.wellbeingDate)
            val cl = itemView.findViewById<ConstraintLayout>(R.id.wellbeingConstraintLayout)
            val options = itemView.findViewById<ImageButton>(R.id.wellbeingOptions)
            score.text = wb.rating.toString()
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val ds = sdf.format(wb.date.timeInMillis)
            date.text = ds
            val db = TeacherPlannerDatabase.getInstance(itemView.context).teacherPlannerDao

            cl.setOnClickListener {
                it.findNavController().navigate(
                    WellbeingFragmentDirections.actionNavigationWellbeingToNavigationViewWellbeing(
                        wb.WellbeingId
                    )
                )
            }

            when (wb.rating) {
                1 -> {
                    options.setBackgroundColor(itemView.resources.getColor(R.color.red))
                    options.setColorFilter(itemView.resources.getColor(R.color.white))
                    date.setTextColor(itemView.resources.getColor(R.color.white))
                    score.setTextColor(itemView.resources.getColor(R.color.white))
                    cl.setBackgroundColor(itemView.resources.getColor(R.color.red))
                }
                2 -> {
                    options.setBackgroundColor(itemView.resources.getColor(R.color.orange))
                    options.setColorFilter(itemView.resources.getColor(R.color.black))
                    date.setTextColor(itemView.resources.getColor(R.color.black))
                    score.setTextColor(itemView.resources.getColor(R.color.black))
                    cl.setBackgroundColor(itemView.resources.getColor(R.color.orange))
                }
                3 -> {
                    options.setBackgroundColor(itemView.resources.getColor(R.color.amber))
                    options.setColorFilter(itemView.resources.getColor(R.color.black))
                    date.setTextColor(itemView.resources.getColor(R.color.black))
                    score.setTextColor(itemView.resources.getColor(R.color.black))
                    cl.setBackgroundColor(itemView.resources.getColor(R.color.amber))
                }
                4 -> {
                    options.setBackgroundColor(itemView.resources.getColor(R.color.yellow))
                    options.setColorFilter(itemView.resources.getColor(R.color.black))
                    date.setTextColor(itemView.resources.getColor(R.color.black))
                    score.setTextColor(itemView.resources.getColor(R.color.black))
                    cl.setBackgroundColor(itemView.resources.getColor(R.color.yellow))
                }
                5 -> {
                    options.setBackgroundColor(itemView.resources.getColor(R.color.green))
                    options.setColorFilter(itemView.resources.getColor(R.color.black))
                    date.setTextColor(itemView.resources.getColor(R.color.black))
                    score.setTextColor(itemView.resources.getColor(R.color.black))
                    cl.setBackgroundColor(itemView.resources.getColor(R.color.green))
                }
            }

            options.setOnClickListener { view ->
                val popup = PopupMenu(view.context, view)
                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.edit_menu_item -> {
                            view.findNavController().navigate(
                                WellbeingFragmentDirections.actionNavigationWellbeingToNavigationEditWellbeing(
                                    wb.WellbeingId
                                )
                            )
                            true
                        }
                        R.id.delete_menu_item -> {
                            val builder: AlertDialog.Builder = AlertDialog.Builder(view.context)
                            builder.apply {
                                setPositiveButton("Delete",
                                    DialogInterface.OnClickListener { dialog, id ->
                                        GlobalScope.launch {
                                            delWell(db, wb.WellbeingId)
                                        }
                                    })
                                setNegativeButton("Cancel",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        //Cancel
                                    })
                            }
                            builder.setTitle("Delete Reflection?")
                                .setMessage("Do you want to delete your reflection from " + ds + "? This cannot be undone.")
                            builder.show()
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
                popup.inflate(R.menu.edit_event_menu)
                popup.show()
            }

        }

        private suspend fun delWell(db: TeacherPlannerDao, id: Long) {
            db.deleteWellbeingWithId(id)
        }

        companion object {
            fun from(parent: ViewGroup): WellbeingAdapter.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val view = layoutInflater
                    .inflate(R.layout.wellbeing_item, parent, false)

                return WellbeingAdapter.ViewHolder(view)
            }
        }

    }

}