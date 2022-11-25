package com.example.tabata.View

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
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
        updateTheme()
    }

    override fun onBackPressed() {

        val setIntent = Intent (this, MainActivity::class.java)

        this.startActivity(setIntent)

        finish()
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


    fun updateTheme() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val theme: Boolean = sharedPref.getBoolean("theme_switch_preference", false)
        val bcg: ConstraintLayout = findViewById(R.id.edit_constraint)
        val sound_title : TextView = findViewById(R.id.soundTitle)
        val workName: EditText = findViewById(R.id.seqTitle)
        val sets_title: TextView = findViewById(R.id.sets_title)
        val color_title: TextView = findViewById(R.id.color_title)

        val font: String? = sharedPref.getString("font_preference", "-1")
        viewModel.font = font!!


        if (font == "1") {
            workName.setTextSize(TypedValue.COMPLEX_UNIT_PX,  (80).toFloat())
            sets_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (80).toFloat())
            color_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (80).toFloat())
            sound_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (80).toFloat())

        }
        else if (font == "3") {
            sets_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (100).toFloat())
            workName.setTextSize(TypedValue.COMPLEX_UNIT_PX,  (100).toFloat())
            color_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (100).toFloat())
            sound_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, (100).toFloat())

        }


        if (theme) {

            bcg.setBackgroundColor(baseContext.resources.getColor(R.color.darktheme))
        }
        else {
            bcg.setBackgroundColor(baseContext.resources.getColor(R.color.white))
        }

    }



            }





