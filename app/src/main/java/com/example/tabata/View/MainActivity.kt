package com.example.tabata.View


import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabata.*
import com.example.tabata.Models.SequenceModel
import com.example.tabata.View.adapters.SequenceRecyclerAdapter
import com.example.tabata.viewModel.MainViewModel
import java.util.*


class MainActivity : AppCompatActivity(), SequenceRecyclerAdapter.ClickListener {

    private lateinit var blogAdapter: SequenceRecyclerAdapter
    private var editMenu :Boolean = false
    lateinit var viewModel: MainViewModel
    private var pressedSequenceId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView(this)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = sharedPref.getBoolean("theme_switch_preference", false)
        val lang = sharedPref.getBoolean("language_switch_preference", false)

        if (lang) {
            setLocale("ru")
        } else {
            setLocale("")
        }
        viewModel = MainViewModel(application)
        viewModel.data.observe(this){
            Log.d("oncreatemy", "${it.lastIndex}")

            blogAdapter.submitList(it.toMutableList())
        }
        updateTheme()
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
        if(!editMenu) {
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
        if(id == R.id.settings){
            val intentToSettingsActivity = Intent(this, SettingsActivity::class.java)
            startActivity(intentToSettingsActivity)
        }
        return super.onOptionsItemSelected(item)
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

    private fun setLocale(language: String) {

        val locale: Locale = Locale(language)
        Locale.setDefault(locale)

        val conf: Configuration = Configuration()
        conf.setLocale(locale)

        baseContext.resources.updateConfiguration(conf, baseContext.resources.displayMetrics)
    }

    fun updateTheme() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val theme : Boolean = sharedPref.getBoolean("theme_switch_preference", false)

        val bcg : ConstraintLayout = findViewById(R.id.card_constr)

        if(theme) {
            bcg.setBackgroundColor(baseContext.resources.getColor(R.color.darktheme))
        }
        else {
            bcg.setBackgroundColor(baseContext.resources.getColor(R.color.white))
        }


    }
}