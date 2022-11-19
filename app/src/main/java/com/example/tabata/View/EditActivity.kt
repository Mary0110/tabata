package com.example.tabata.View

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.withTransaction
import com.example.tabata.Db.MyDb
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.PhaseType
import com.example.tabata.Models.SequenceModel
import com.example.tabata.R
import com.example.tabata.viewModel.EditViewModel
import com.example.tabata.databinding.ActivityEditBinding
import com.example.tabata.viewModel.MyViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditActivity : AppCompatActivity() {

    private lateinit var phaseAdapter: PhaseRecyclerAdapter
    private  var currentSequenceId: Int? = 0

    lateinit var editViewModel: EditViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_edit)

        val editBinding: ActivityEditBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_edit
        )
        currentSequenceId = intent.getIntExtra("sequence_id", 0)

        val viewModel: EditViewModel by viewModels { MyViewModelFactory(getApplication(), currentSequenceId!!) }
        editViewModel = viewModel
        Log.d("my", "${editViewModel.phasesList.value}")
        initRecyclerView()
        editViewModel.phasesList.observe(this){
            phaseAdapter.submitList(it.toList())
        }
//        val list = editViewModel.phasesList.value
//        if(list != null)
//        {phaseAdapter.submitList(list.toList())
//        phaseAdapter.notifyDataSetChanged()}
        //editViewModel = ViewModelProvider(this)[EditViewModel::class.java]

        editBinding.lifecycleOwner = this

        // Pass the ViewModel into the binding
        editBinding.viewmodel = editViewModel
        //phaseAdapter.submitList(editViewModel.phasesList.value as List<PhaseModel>)
        val current_sequence = intent.getIntExtra("sequence_id", 0)
       /* if(current_sequence != 0)
            readFromDb(current_sequence)*/




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
    private fun insertPhase(type: PhaseType) {
        var size = 0
        if(editViewModel.phasesList.value?.size != null)
            size = editViewModel.phasesList.value?.size!!
        Log.d("currseqid", "$currentSequenceId")
        val newPhase = PhaseModel(phaseId = null,sequenceId = currentSequenceId, phaseType = type, order = size+1)
        editViewModel.phasesList.value?.add(newPhase)
        phaseAdapter.submitList(editViewModel.phasesList.value!!.toList())
        phaseAdapter.notifyItemInserted(size)
    }

fun setValues(seq:SequenceModel){
    editViewModel.setTitle(seq.title)
    editViewModel.setColor(seq.color)
    editViewModel.setSets(seq.sets_number)
}


            }





