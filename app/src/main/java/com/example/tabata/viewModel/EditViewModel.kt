package com.example.tabata.viewModel

import android.app.Application
import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.*
import androidx.room.withTransaction
import com.example.tabata.Db.MyDb
import com.example.tabata.Db.Repo
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.SequenceWithPhases
import com.example.tabata.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class EditViewModel(application: Application, mParam: Int): AndroidViewModel(application) {
    var phasesList = MutableLiveData<MutableList<PhaseModel>>()
    var title = MutableLiveData<String>()
    var sets = MutableLiveData<Int>()
    var color = MutableLiveData<Int>()
    lateinit var curr: SequenceWithPhases
    lateinit var repo : Repo
   // var data: LiveData<List<SequenceWithPhases>>
    init{
        var db = MyDb.getDb(application)
        repo = Repo(db)
       viewModelScope.launch {
           curr = repo.getAll().find { it.sequence.sequenceId == mParam }!!
           title.value = curr.sequence.title
           Log.d("my", "${title.value}")
           sets.value = curr.sequence.sets_number
           color.value = curr.sequence.color
           phasesList.value = curr.phases.toMutableList()
       }



    }

    fun onSplitTypeChanged(radioGroup: RadioGroup?, id: Int) {
        color.value = id
    }

    fun setTitle(_title: String){
        title.postValue( _title)
    }

    fun setSets(_title: Int){
        sets.postValue(_title)
    }

    fun setColor(_title: Int){
        color.postValue( _title)
    }

    fun OnMinusSets(){
        if(sets.value!! > 1)
            sets.value = sets.value!! -1
    }
    fun OnPlusSets(){
        if(sets.value!! < 600)
            sets.value = sets.value!! +1
    }

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        title.postValue(s.toString())
        Log.w("tag", "onTextChanged $s")
    }
//    private fun readFromDb(seqId: Int) {
//        val db = MyDb.getDb(this)
//        viewModelScope.launch(Dispatchers.IO) {
//            db.withTransaction {
//                val phases = db.getDao().getPhases(seqId)
//                phaseAdapter.submitList(phases)
//                val seq = db.getDao().getSequence(seqId)
//                setValues(seq)
//            }
//        }
//    }
}