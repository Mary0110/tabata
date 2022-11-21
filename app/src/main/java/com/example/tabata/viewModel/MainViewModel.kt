package com.example.tabata.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tabata.Db.MyDb
import com.example.tabata.Db.Repo
import com.example.tabata.Models.SequenceModel
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){

    lateinit var repo: Repo
    lateinit var data: LiveData<List<SequenceModel>>
    init{
        val db = MyDb.getDb(application)
        repo = Repo(db)
        updateData()
    }

    fun deleteSequence(pressedSequenceId: Long) {
        val r = data.value?.find{
            it.SequenceId == pressedSequenceId
        }
        viewModelScope.launch {
            if (r != null) {
                repo.removeSequence(r)
            }
        }
    }

    fun updateData(){
        data = repo.getSequences()
    }
}