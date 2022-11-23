package com.example.tabata.View


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.withTransaction
import com.example.tabata.*
import com.example.tabata.Db.MyDb
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.PhaseType
import com.example.tabata.Models.SequenceModel
import com.example.tabata.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), SequenceRecyclerAdapter.ClickListener {

    private lateinit var blogAdapter: SequenceRecyclerAdapter
    //private val myDbManager = MyDbManager(this)
    private var editMenu :Boolean = false
    lateinit var viewModel: MainViewModel
    private var pressedSequenceId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //addDataSet()
        initRecyclerView(this)
        viewModel= MainViewModel(application)
        viewModel.data.observe(this){
            Log.d("oncreatemy", "${it.lastIndex}")

            blogAdapter.submitList(it.toMutableList())
        }

    }

    override fun onRestart() {
        super.onRestart()
        invalidateOptionsMenu()
        viewModel.data.observe(this){
            Log.d("onrestartmy", "${it.lastIndex}")
            blogAdapter.submitList(it.toMutableList())
        }
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
        viewModel.updateData()
        viewModel.data.observe(this){
            Log.d("myonres", "${it.lastIndex}")

            blogAdapter.submitList(it.toMutableList())
        }
    }

    // create an action bar button
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(editMenu == false) {
            menuInflater.inflate(R.menu.mymenu, menu)
        }
        else {
            menuInflater.inflate(R.menu.mymenu2, menu)
            editMenu = false
        }
        return super.onCreateOptionsMenu(menu)
    }



    // handle button activities
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.mybutton) {
                val intentToEditActivity = Intent(this, EditActivity::class.java)

            startActivity(intentToEditActivity)
                Toast.makeText(this@MainActivity, "Yoo clicked plus", Toast.LENGTH_LONG).show()
        }

        if (id == R.id.pencil) {
            val intentToEditActivity = Intent(this, EditActivity::class.java)
            intentToEditActivity.putExtra("sequence_id", pressedSequenceId)
            startActivity(intentToEditActivity)
            Toast.makeText(this@MainActivity, "Yoo clicked pencil", Toast.LENGTH_LONG).show()
        }
        if (id == R.id.delete) {
            if(viewModel.data.value != null)
            { viewModel.deleteSequence(pressedSequenceId)
            Toast.makeText(this@MainActivity, "Yoo clicked pencil", Toast.LENGTH_LONG).show()}
            else{
                editMenu = false
                invalidateOptionsMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun addDataSet(){
        val db = MyDb.getDb(this)
        lifecycleScope.launch(Dispatchers.IO) {
            val seqId = 1
            val seq = SequenceModel( 1,
                "FirstWorkout",
                R.id.radioButton3,
                1,
                true)
            val seq2 = SequenceModel( 2,
                "se",
                R.id.radioButton3,
                1,
                true)

            val phase1 = PhaseModel(
                phaseId = 1,
                sequenceId = 1,
                phaseType = PhaseType.WORK,
                title = "workPhase",
                duration = 1,
                order = 1,

            )
            val phase2 = PhaseModel(
                phaseId = 2,
                sequenceId = 1,
                phaseType = PhaseType.BREAK,
                title = "breakPhase",
                duration = 1,
                order = 2,

            )

            val phase3 = PhaseModel(
                phaseId = null,
                sequenceId = 1,
                phaseType = PhaseType.BREAK,
                title = "breakPhase",
                duration = 3,
                order = 3,

                )

            db.withTransaction {
                db.getDao().insertSequence(seq)
                db.getDao().insertSequence(seq2)

                db.getDao().insertPhase(phase1)
                db.getDao().insertPhase(phase2, phase3)

                val result2 = db.getDao().getAllSequencesWithPhases()

                Log.d("myinsert", "$result2")

                //blogAdapter.submitList(result)
            }
        }
    }




    private fun initRecyclerView(listener: SequenceRecyclerAdapter.ClickListener){
        val recyclerView : RecyclerView= findViewById(R.id.recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(90)
            addItemDecoration(topSpacingDecorator)
            blogAdapter = SequenceRecyclerAdapter(listener)
            adapter = blogAdapter
        }
    }


    override fun onClick(sequence: SequenceModel) {
        Toast.makeText(this@MainActivity, "Yoo clicked sequence", Toast.LENGTH_LONG).show()
        val intentToTimerActivity = Intent(this, TimerActivity::class.java)
        intentToTimerActivity.putExtra("id", sequence.SequenceId)



        startActivity(intentToTimerActivity)
    }

    override fun onLongClick(sequence: SequenceModel) {
        Toast.makeText(this@MainActivity, "Yoo longclicked sequence", Toast.LENGTH_LONG).show()
        pressedSequenceId = sequence.SequenceId!!
        editMenu = true
        invalidateOptionsMenu()
    }
}