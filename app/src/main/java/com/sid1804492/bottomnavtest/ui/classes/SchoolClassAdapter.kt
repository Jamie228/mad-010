package com.sid1804492.bottomnavtest.ui.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.sid1804492.bottomnavtest.R
import com.sid1804492.bottomnavtest.database.SchoolClass
import kotlinx.android.synthetic.main.class_list_item.view.*

class SchoolClassAdapter: RecyclerView.Adapter<SchoolClassAdapter.ViewHolder>() {

    var data = listOf<SchoolClass>()
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

    class ViewHolder private constructor (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val classDetails: TextView = itemView.findViewById(R.id.classListText)
        val editButton: ImageButton = itemView.findViewById(R.id.editClassButton)

        fun bind(item: SchoolClass) {
            val res = itemView.context.resources

            classDetails.text = item.SetName + " - " + item.SubjectName + " - " + item.Room
            classDetails.setOnClickListener { view ->
                view.findNavController().navigate(ClassesFragmentDirections.actionNavigationClassesToNavigationViewClass(item.ClassId))
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val view = layoutInflater
                    .inflate(R.layout.class_list_item, parent, false)

                return ViewHolder(view)
            }
        }
    }

}