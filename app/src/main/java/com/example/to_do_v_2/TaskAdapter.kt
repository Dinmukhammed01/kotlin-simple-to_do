package com.example.to_do_v_2

import android.database.Cursor
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private var tasks: Cursor?) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(itemView)
    }

    // No need for getItemAtPosition method since we are using a Cursor

    fun updateTasks(newTasks: Cursor?) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        tasks?.moveToPosition(position)

        val taskId = tasks?.getLong(tasks?.getColumnIndex(DBHelper.ID_COL) ?: -1)
        val title = tasks?.getString(tasks?.getColumnIndex(DBHelper.TITLE_COL) ?: -1)
        val isCompleted = tasks?.getInt(tasks?.getColumnIndex(DBHelper.IS_COMPLETED_COL) ?: -1) == 1

        if (taskId != null && title != null) {
            holder.titleTextView.text = title
            holder.checkBox.isChecked = isCompleted

            if (isCompleted) {
                holder.titleTextView.setTextColor(Color.GRAY)
            } else {
                holder.titleTextView.setTextColor(Color.BLACK)
            }

            holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
                // Handle checkbox state change
                // You may want to update the database here
            }
        }
    }

    fun getItemAtPosition(position: Int): Task {
        tasks?.moveToPosition(position)

        val taskId = tasks?.getLong(tasks?.getColumnIndex(DBHelper.ID_COL) ?: -1)
        val title = tasks?.getString(tasks?.getColumnIndex(DBHelper.TITLE_COL) ?: -1)
        val isCompleted = tasks?.getInt(tasks?.getColumnIndex(DBHelper.IS_COMPLETED_COL) ?: -1) == 1

        return Task(taskId ?: -1, title ?: "", isCompleted)
    }

    override fun getItemCount(): Int {
        return tasks?.count ?: 0
    }
}
