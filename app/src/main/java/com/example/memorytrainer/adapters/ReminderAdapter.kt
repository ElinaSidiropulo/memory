package com.example.memorytrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memorytrainer.R
import java.util.Date

import com.example.memorytrainer.models.Reminder;

class ReminderAdapter(
    private val reminders: List<Reminder>,
    private val onDelete: (Reminder) -> Unit,
    private val onEdit: (Reminder) -> Unit
) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvTime: TextView = view.findViewById(R.id.tv_time)
        val btnDelete: Button = view.findViewById(R.id.btn_delete)
        val btnEdit: Button = view.findViewById(R.id.btn_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = reminders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reminder = reminders[position]
        holder.tvTitle.text = reminder.title
        holder.tvTime.text = Date(reminder.timeInMillis).toString()

        holder.btnDelete.setOnClickListener { onDelete(reminder) }
        holder.btnEdit.setOnClickListener { onEdit(reminder) }
    }
}
