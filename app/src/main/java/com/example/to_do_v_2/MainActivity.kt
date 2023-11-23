package com.example.to_do_v_2

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.task_list)
        val addBtn: Button = findViewById(R.id.ADD_btn)
        val renameBtn: Button = findViewById(R.id.removeTask_btn)
        val deleteBtn: Button = findViewById(R.id.deleteTask_btn)
        val taskEditText: EditText = findViewById(R.id.txt_task)

        dbHelper = DBHelper(this)
        taskAdapter = TaskAdapter(getAllTasks())

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = taskAdapter
        }

        addBtn.setOnClickListener {
            val taskTitle = taskEditText.text.toString()
            if (taskTitle.isNotBlank()) {
                dbHelper.addTask(taskTitle, false)
                updateTaskList()
                taskEditText.text.clear()
            } else {
                Toast.makeText(this, "Task title cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        renameBtn.setOnClickListener {
            val selectedTask = taskAdapter.getItemAtPosition(getSelectedTaskPosition(recyclerView))
            val newTitle = taskEditText.text.toString()
            if (newTitle.isNotBlank()) {
                dbHelper.updateTask(selectedTask.id, newTitle, selectedTask.isCompleted)
                updateTaskList()
                taskEditText.text.clear()
            } else {
                Toast.makeText(this, "новый заголовок не может быть пустым", Toast.LENGTH_SHORT).show()
            }
        }

        deleteBtn.setOnClickListener {
            val selectedTask = taskAdapter.getItemAtPosition(getSelectedTaskPosition(recyclerView))
            dbHelper.deleteTask(selectedTask.id)
            updateTaskList()
            taskEditText.text.clear()
        }
    }

    private fun getAllTasks(): Cursor? {
        return dbHelper.getAllTasks()
    }

    private fun updateTaskList() {
        taskAdapter.updateTasks(getAllTasks())
    }

    private fun getSelectedTaskPosition(recyclerView: RecyclerView): Int {
        return (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    }
}
