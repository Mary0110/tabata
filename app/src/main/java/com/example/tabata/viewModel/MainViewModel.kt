package com.example.tabata.viewModel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.tabata.Db.MyDb
import com.example.tabata.Db.Repo
import com.example.tabata.Models.SequenceModel
import com.example.tabata.Models.SequenceWithPhases
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){

    lateinit var repo: Repo
    lateinit var data: LiveData<List<SequenceWithPhases>>
    //
    init{
        val db = MyDb.getDb(application)
        repo = Repo(db)
        updateData()
    }


    fun deleteSequence(pressedSequenceId: Long) {
        val r = data.value?.find{
            it.sequence.SequenceId == pressedSequenceId
        }
        viewModelScope.launch {
            if (r != null) {
                repo.removeSequence(r.sequence)
            }
        }
    }

    fun updateData(){
        data = repo.getSequencesWithPhasesLive()

    }

}