package com.example.tabata.View


import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference

import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.tabata.R
import com.example.tabata.Db.MyDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Suppress("DEPRECATION")
class SettingsFragment : PreferenceFragmentCompat() {

    lateinit var activity: SettingsActivity

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val delPreference : Preference = findPreference("delete_preference")
        val langPreference : Preference = findPreference("language_switch_preference")
        val themePreference : Preference = findPreference("theme_switch_preference")
        val fontPreference : Preference = findPreference("font_preference")

        delPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val dao = MyDb.getDb(requireContext()).getDao()
            lifecycleScope.launch(Dispatchers.IO){
                dao.deleteAllSequences()
            }
            Toast.makeText(context, getString(R.string.data_cleaned_msg), Toast.LENGTH_SHORT).show()
            true
        }

        themePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            activity.refreshFragment()

            true
        }

        langPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            val language : String = if ( sharedPref.getBoolean("language_switch_preference", false) ) { "ru" } else { "" }

            val locale: Locale = Locale(language)
            Locale.setDefault(locale)

            val conf: Configuration = Configuration()
            conf.setLocale(locale)

            context?.resources?.updateConfiguration(conf, context?.resources?.displayMetrics)

            activity.refreshFragment()

            true
        }

        fontPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            activity.refreshFragment()

            true
        }

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val font : String? = sharedPref.getString("font_preference", "-1")
    }
}