package com.example.myapplication.todolist

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.todolist.db.TodoContract
import com.example.myapplication.todolist.db.TodoDbHelper
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity() {
    private var editText: EditText? = null
    private var addBtn: Button? = null
    private var priority = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_todo)
        setTitle(R.string.take_a_note)
        editText = findViewById(R.id.edit_text)
        editText!!.setFocusable(true)
        editText!!.requestFocus()
        val inputMethod = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethod?.showSoftInput(editText, 0)
        addBtn = findViewById(R.id.btn_add)
        addBtn!!.setOnClickListener(View.OnClickListener {
            val content: CharSequence = editText!!.getText()
            if (TextUtils.isEmpty(content)) {
                Toast.makeText(
                    this@NoteActivity,
                    "No content to add", Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val succeed = saveNote2Database(content.toString().trim { it <= ' ' })
            if (succeed) {
                Toast.makeText(
                    this@NoteActivity,
                    "Note added", Toast.LENGTH_SHORT
                ).show()
                setResult(RESULT_OK)
            } else {
                Toast.makeText(
                    this@NoteActivity,
                    "Error", Toast.LENGTH_SHORT
                ).show()
            }
            finish()
        })
        val pro = findViewById<View>(R.id.RadioGroup1) as RadioGroup //?????????????????????
        //????????????????????????????????????
        pro.setOnCheckedChangeListener { radioGroup, i ->
            val RB = findViewById<View>(i) as RadioButton //??????????????????????????????
            if (RB.id == 2131230814) {
                priority = 3
                //???????????????3
                Log.i("NoteActivity", "In RadioGroup:lp")
            }
            if (RB.id == 2131230840) {
                priority = 2
                //???????????????2
                Log.i("NoteActivity", "In RadioGroup:mp")
            }
            if (RB.id == 2131230835) {
                priority = 1
                //???????????????1
                Log.i("NoteActivity", "In RadioGroup:hp")
            }
            Log.i(
                "data", "??????????????????" + RB.text + " id:" + RB.id +
                        " priortiy: " + priority.toString()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun saveNote2Database(content: String): Boolean {
        // TODO ????????????????????????????????????????????????
        val simpleDateFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH)
        val date = Date(System.currentTimeMillis())
        val dbHelper = TodoDbHelper(this)
        val db: SQLiteDatabase = dbHelper.getWritableDatabase()
        val values = ContentValues()
        values.put(TodoContract.TodoEntry.COLUMN_CONTENT, content)
        values.put(TodoContract.TodoEntry.COLUMN_DATE, simpleDateFormat.format(date))
        // TODO ??????
        values.put(TodoContract.TodoEntry.COLUMN_STATE, 0)
        values.put(TodoContract.TodoEntry.COLUMN_PRIORTY, priority)
        val newRowId = db.insert(TodoContract.TodoEntry.TABLE_NAME, null, values)
        return newRowId != 0L
    }
}
