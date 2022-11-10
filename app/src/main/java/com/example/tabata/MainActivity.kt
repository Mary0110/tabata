package com.example.tabata


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), SequenceRecyclerAdapter.ClickListener {

    private lateinit var blogAdapter: SequenceRecyclerAdapter
    private val myDbManager = MyDbManager(this)
    private var editMenu :Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initRecyclerView(this)
        addDataSet()
    }

    override fun onResume() {
        invalidateOptionsMenu()
        super.onResume()
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
                val intentToConverterScreen = Intent(this, EditActivity::class.java)
                startActivity(intentToConverterScreen)
                Toast.makeText(this@MainActivity, "Yoo clicked plus", Toast.LENGTH_LONG).show()
        }

        if (id == R.id.pencil) {
            val intentToConverterScreen = Intent(this, EditActivity::class.java)
            startActivity(intentToConverterScreen)
            Toast.makeText(this@MainActivity, "Yoo clicked pencil", Toast.LENGTH_LONG).show()
        }
            // do something here

        return super.onOptionsItemSelected(item)
    }

    private fun addDataSet(){
        val data : ArrayList<SequenceModel> = myDbManager.readDbData()
        data.addAll(DataSource.createDataSet())
        blogAdapter.submitList(data)
    }




    private fun initRecyclerView(listener:SequenceRecyclerAdapter.ClickListener){
        val recyclerView : RecyclerView= findViewById(R.id.recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            val topSpacingDecorator = TopSpacingItemDecoration(90)
            addItemDecoration(topSpacingDecorator)
            blogAdapter = SequenceRecyclerAdapter(listener)
            adapter = blogAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()

    }

    override fun onClick(sequence: SequenceModel) {
        Toast.makeText(this@MainActivity, "Yoo clicked sequence", Toast.LENGTH_LONG).show()
        val intentToTimerActivity = Intent(this, TimerActivity::class.java)
        startActivity(intentToTimerActivity)
    }

    override fun onLongClick(sequence: SequenceModel) {
        Toast.makeText(this@MainActivity, "Yoo longclicked sequence", Toast.LENGTH_LONG).show()
        editMenu = true
        invalidateOptionsMenu()
    }
}