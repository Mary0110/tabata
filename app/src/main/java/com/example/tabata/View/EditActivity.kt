package com.example.tabata.View

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.withTransaction
import com.example.tabata.Db.MyDb
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.SequenceModel
import com.example.tabata.R
import com.example.tabata.viewModel.EditViewModel
import com.example.tabata.databinding.ActivityEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditActivity : AppCompatActivity() {

    //private val myDbManager = MyDbManager(this)
    private lateinit var phaseAdapter: PhaseRecyclerAdapter

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
        val current_sequence = intent.getIntExtra("sequence_id", 0)
        if(current_sequence != 0)
            readFromDb(current_sequence)
        initRecyclerView()



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

    private fun initRecyclerView(){
        val recyclerView : RecyclerView = findViewById(R.id.phase_recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@EditActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(90)
            addItemDecoration(topSpacingDecorator)
            phaseAdapter = PhaseRecyclerAdapter()
            adapter = phaseAdapter
            //phaseAdapter.submitList(result)
        }
    }
fun setValues(seq:SequenceModel){
    editViewModel.setTitle(seq.title)
    editViewModel.setColor(seq.color)
    editViewModel.setSets(seq.sets_number)
}
    private fun readFromDb(seqId: Int){
    val db = MyDb.getDb(this)
        lifecycleScope.launch(Dispatchers.IO) {
            db.withTransaction {
                val phases = db.getDao().getPhases(seqId)
                phaseAdapter.submitList(phases)
                val seq = db.getDao().getSequence(seqId)
                setValues(seq)
            }

            }
        }

    }




