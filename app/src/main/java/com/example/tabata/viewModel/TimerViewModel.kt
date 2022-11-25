package com.example.tabata.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.tabata.Db.MyDb
import com.example.tabata.Db.Repo
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.SequenceModel
import com.example.tabata.R
import kotlinx.coroutines.launch

class TimerViewModel(application: Application, val mParam: Long): AndroidViewModel(application) {

    var repo: Repo
    var phasesList = MutableLiveData<List<PhaseModel>>(emptyList())
    //
    init{
        val db = MyDb.getDb(application)
        repo = Repo(db)
        if(mParam!= 0L) {
            loadData(application, mParam)
        }

    }

    private fun loadData(application: Application, mParam:Long) {

        viewModelScope.launch {
            var curr = repo.getAll().find { it.sequence.SequenceId == mParam }!!
            phasesList.value = ((curr.phases))


        }
    }
}