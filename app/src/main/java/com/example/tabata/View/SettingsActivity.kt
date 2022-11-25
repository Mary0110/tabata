package com.example.tabata.View

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tabata.R


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        val frg: SettingsFragment = SettingsFragment()
        frg.activity = this

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, frg)
            .commit()

        updateTheme()
    }

    fun refreshFragment() {
        val frg: SettingsFragment = SettingsFragment()
        frg.activity = this

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container_view, frg)
            .commit()

        updateTheme()

    }

    private fun updateTheme() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val theme : Boolean = sharedPref.getBoolean("theme_switch_preference", false)
        val bcg : ConstraintLayout = findViewById(R.id.settings_constraint_layout)
        //TODO:theme
        if(theme) {
            bcg.setBackgroundColor(baseContext.resources.getColor(R.color.darktheme))
        }
        else {
            bcg.setBackgroundColor(baseContext.resources.getColor(R.color.white))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val myIntent = Intent(this, MainActivity::class.java)

        this.startActivity(myIntent)

        finish()

        return true
    }

    override fun onBackPressed() {
        val myIntent = Intent(this, MainActivity::class.java)

        this.startActivity(myIntent)

        finish()
    }
}