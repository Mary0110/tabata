package com.example.tabata.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tabata.Db.MyDb
import com.example.tabata.Db.Repo
import com.example.tabata.Models.SequenceModel

class MainViewModel(application: Application) : AndroidViewModel(application){
    val data: LiveData<List<SequenceModel>>
    init{
        val db = MyDb.getDb(application)
        val repo = Repo(db)
        data = repo.getSequences()
    }
}