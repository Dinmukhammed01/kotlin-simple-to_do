package com.example.to_do_v_2
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TODO_DATABASE"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "Tasks"
        const val ID_COL = "id"
        const val TITLE_COL = "title"
        const val IS_COMPLETED_COL = "is_completed"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE $TABLE_NAME ("
                + "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$TITLE_COL TEXT, "
                + "$IS_COMPLETED_COL INTEGER DEFAULT 0" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addTask(title: String, isCompleted: Boolean) {
        val values = ContentValues()
        values.put(TITLE_COL, title)
        values.put(IS_COMPLETED_COL, if (isCompleted) 1 else 0)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllTasks(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun updateTask(taskId: Long, newTitle: String, isCompleted: Boolean) {
        val values = ContentValues()
        values.put(TITLE_COL, newTitle)
        values.put(IS_COMPLETED_COL, if (isCompleted) 1 else 0)

        val db = this.writableDatabase
        db.update(TABLE_NAME, values, "$ID_COL = ?", arrayOf(taskId.toString()))
        db.close()
    }

    fun deleteTask(taskId: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID_COL = ?", arrayOf(taskId.toString()))
        db.close()
    }
}
