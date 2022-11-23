package com.example.tabata.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.PhaseType
import com.example.tabata.Models.SequenceModel
import com.example.tabata.R
import com.example.tabata.databinding.ActivityEditBinding
import com.example.tabata.viewModel.EditViewModel
import com.example.tabata.viewModel.MyViewModelFactory
import com.example.tabata.viewModel.PhaseViewModel



class EditActivity : AppCompatActivity() {

    private  var currentSequenceId: Long? = 0
    lateinit var viewModel: EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        currentSequenceId = intent.getLongExtra("sequence_id", 0)
        Log.d("seqmy", "$currentSequenceId")
        val editViewModel: EditViewModel by viewModels { MyViewModelFactory(getApplication(), currentSequenceId!!) }
        viewModel = editViewModel
        val binding = ActivityEditBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = editViewModel
        setContentView(binding.root)
//
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_editor, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.save_button) {
            viewModel.saveSequence()
            Log.d("mycff", "")
            val intentToTimerScreen = Intent(this, TimerActivity::class.java)
            intentToTimerScreen.putExtra("id", currentSequenceId)
            startActivity(intentToTimerScreen)
            Toast.makeText(this@EditActivity, "Yoo clicked save", Toast.LENGTH_LONG).show()
            finish()

        }

        else if (id == R.id.work_submenu)
        {
            insertPhase(PhaseType.WORK)
            Toast.makeText(this@EditActivity, "Yoo clicked 1", Toast.LENGTH_LONG).show()

        }
        else if (id == R.id.break_submenu)
        {
            insertPhase(PhaseType.BREAK)
            Toast.makeText(this@EditActivity, "Yoo clicked 2", Toast.LENGTH_LONG).show()
        }
        else if (id == R.id.long_break_submenu)
        {
            insertPhase(PhaseType.LONG_BREAK)
            Toast.makeText(this@EditActivity, "Yoo clicked 2", Toast.LENGTH_LONG).show()
        }
        else if (id == R.id.warm_up_submenu)
        {
            insertPhase(PhaseType.WARM_UP)
            Toast.makeText(this@EditActivity, "Yoo clicked 1", Toast.LENGTH_LONG).show()

        }
        // do something here
        return super.onOptionsItemSelected(item)
    }

    private fun insertPhase(type: PhaseType) {

        var id = viewModel.getLastAddedId() + 1
        val newPhase = PhaseViewModel(type = type, phaseId = id)
        viewModel.phasesList.value = viewModel.phasesList.value?.plus(newPhase)
    }



    private fun addDataSet() {
        var phases: MutableList<PhaseModel>

    }

        fun setValues(seq:SequenceModel){
   // viewModel.setTitle(seq.title)
    viewModel.setColor(seq.color)
    viewModel.setSets(seq.sets_number)
}


            }





