package com.example.tabata.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.withTransaction
import com.example.tabata.Db.MyDb
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.PhaseType
import com.example.tabata.Models.SequenceModel
import com.example.tabata.R
import com.example.tabata.databinding.ActivityEditBinding
import com.example.tabata.viewModel.EditViewModel
import com.example.tabata.viewModel.ItemViewModel
import com.example.tabata.viewModel.MyViewModelFactory
import com.example.tabata.viewModel.PhaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditActivity : AppCompatActivity() {

   /// private lateinit var phaseAdapter: PhaseRecyclerAdapter
    private  var currentSequenceId: Long? = 0

    lateinit var viewModel: EditViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_edit)

       /* val editBinding: ActivityEditBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_edit
        )*/
        currentSequenceId = intent.getLongExtra("sequence_id", 0)
        Log.d("seqmy", "$currentSequenceId")
        val editViewModel: EditViewModel by viewModels { MyViewModelFactory(getApplication(), currentSequenceId!!) }
        viewModel = editViewModel
        val binding = ActivityEditBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = editViewModel
        setContentView(binding.root)
//        binding.viewModel.title.observe(this){
//            binding.seqTitle.setText("blalalala")
//        }
//        val vfd = findViewById<TextView>(R.id.seqTitle)
//        viewModel.title.observe(this){
//            vfd.text = it
//        }
        /*editViewModel = viewModel
        Log.d("my", "${editViewModel.phasesList.value}")
        initRecyclerView()
        editViewModel.phasesList.observe(this){
            phaseAdapter.submitList(it.toList())
        }
//
        editBinding.lifecycleOwner = this

        editBinding.viewmodel = editViewModel*/
        //phaseAdapter.submitList(editViewModel.phasesList.value as List<PhaseModel>)
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
            viewModel.saveSequence()
            Log.d("mycff", "")
            //viewModel.savePhases()
            //TODO:saveSequence()
            val intentToTimerScreen = Intent(this, TimerActivity::class.java)
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

   /* private fun initRecyclerView(){
        val recyclerView : RecyclerView = findViewById(R.id.phase_recyclerView)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@EditActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(90)
            addItemDecoration(topSpacingDecorator)
            phaseAdapter = PhaseRecyclerAdapter()
            adapter = phaseAdapter
            //phaseAdapter.submitList(result)
        }
    }*/
    private fun insertPhase(type: PhaseType) {
        var size = 0
        if(viewModel.phasesList.value?.size != null)
            size = viewModel.phasesList.value?.size!!
        //Log.d("currseqid", "$currentSequenceId")
        val newPhase = PhaseViewModel(type = type)
        viewModel.phasesList.value = viewModel.phasesList.value?.plus(newPhase)
        //phaseAdapter.submitList(viewModel.phasesList.value!!.toList())
            //phaseAdapter.notifyItemInserted(size-1)
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





