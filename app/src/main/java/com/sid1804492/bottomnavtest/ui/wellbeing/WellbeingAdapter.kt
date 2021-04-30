package com.sid1804492.bottomnavtest.ui.wellbeing

import android.graphics.Color.red
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.Wellbeing
import com.sid1804492.bottomnavtest.ui.todo.TodoAdapter
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