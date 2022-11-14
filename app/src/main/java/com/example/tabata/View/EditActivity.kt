package com.example.tabata.View

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.tabata.R
import com.example.tabata.viewModel.EditViewModel
import com.example.tabata.databinding.ActivityEditBinding


class EditActivity : AppCompatActivity() {

    //private val myDbManager = MyDbManager(this)
    lateinit var editViewModel: EditViewModel
   // private lateinit var homeBinding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_edit)
        editViewModel = ViewModelProvider(this)[EditViewModel::class.java]

        val editBinding: ActivityEditBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_edit
        )
        editBinding.lifecycleOwner = this

        // Pass the ViewModel into the binding
        editBinding.viewmodel = editViewModel




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_editor, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.save_button) {
            //TODO:saveSequence()
            val intentToTimerScreen = Intent(this, TimerActivity::class.java)
            startActivity(intentToTimerScreen)

            Toast.makeText(this@EditActivity, "Yoo clicked save", Toast.LENGTH_LONG).show()
        }
        else if (id == R.id.work_submenu)
        {
            //TODO:addWork()
            Toast.makeText(this@EditActivity, "Yoo clicked 1", Toast.LENGTH_LONG).show()

        }
        else if (id == R.id.break_submenu)
        {
            //TODO:addBreak()
            Toast.makeText(this@EditActivity, "Yoo clicked 2", Toast.LENGTH_LONG).show()
        }
        // do something here
        return super.onOptionsItemSelected(item)
    }

   /* private fun saveSequence() {
        editViewModel = ViewModelProvider(this)[EditViewModel::class.java]
        var title =  editViewModel.title.value.toString()
        var color =  editViewModel.color.value
        var warm_up_time =  editViewModel.warmup_time.value
        var sets_number =  editViewModel.sets.value
        var workout_time =  editViewModel.workout_time.value
        var break_time =  editViewModel.break_time.value
        var repetitions_number =  editViewModel.repetitions_num.value
        var long_break_time =  editViewModel.long_break_time.value
        var sequence  = SequenceModel(
            null,
            title,
            color!!,
            warm_up_time!!,
            sets_number!!,
            workout_time!!,
            break_time!!,
            repetitions_number!!,
            long_break_time!!)



       // myDbManager.insertIntoDb()
    }*/


}
