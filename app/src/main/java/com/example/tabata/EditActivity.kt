package com.example.tabata

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class EditActivity : AppCompatActivity() {

    private val myDbManager = MyDbManager(this)
    lateinit var editViewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        editViewModel = ViewModelProvider(this)[EditViewModel::class.java]

        val title = findViewById<EditText>(R.id.editTextTitle)
        val work_time = findViewById<TextView>(R.id.work_time_num)
        val break_time = findViewById<TextView>(R.id.break_time_num)
        val sets_num = findViewById<TextView>(R.id.s_num)
        val rep_num = findViewById<TextView>(R.id.rep_num)
        val warmup_time = findViewById<TextView>(R.id.warmup_time_num)
        val long_break_time = findViewById<TextView>(R.id.long_break_time_num)


        editViewModel.workout_time.observe(this, Observer {
            work_time.text = it.toString()
        })
        editViewModel.break_time.observe(this, Observer {
            break_time.text = it.toString()
        })
        editViewModel.sets.observe(this, Observer {
            sets_num.text = it.toString()
        })
        editViewModel.repetitions_num.observe(this, Observer {
            rep_num.text = it.toString()
        })
        editViewModel.warmup_time.observe(this, Observer {
            warmup_time.text = it.toString()
        })
        editViewModel.long_break_time.observe(this, Observer {
            long_break_time.text = it.toString()
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_editor, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.save_button) {
            saveSequence()
            val intentToConverterScreen = Intent(this, TimerActivity::class.java)
            startActivity(intentToConverterScreen)
            Toast.makeText(this@EditActivity, "Yoo clicked save", Toast.LENGTH_LONG).show()
        }
        // do something here

        return super.onOptionsItemSelected(item)
    }

    private fun saveSequence() {
       // myDbManager.insertIntoDb()
    }

}
