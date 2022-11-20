package com.example.tabata.viewModel

import android.app.Application
import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.*
import com.example.tabata.Db.MyDb
import com.example.tabata.Db.Repo
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.SequenceWithPhases
import kotlinx.coroutines.launch


class EditViewModel(application: Application, mParam: Int): AndroidViewModel(application) {
    var phasesList = MutableLiveData<List<ItemViewModel>>(emptyList())

    private var _title = MutableLiveData<String>()
    var sets = MutableLiveData<Int>()
    var color = MutableLiveData<Int>()
    lateinit var curr: SequenceWithPhases
    lateinit var repo : Repo
    val data: LiveData<List<ItemViewModel>>
        get() = phasesList
    val title: LiveData<String>
        get() = _title
   // var data: LiveData<List<SequenceWithPhases>>
    init{
       loadData(application, mParam)
       Log.d("myinitialized", "init")

    }

    private fun loadData(application: Application, mParam:Int) {
        var db = MyDb.getDb(application)
        repo = Repo(db)
        viewModelScope.launch {
            curr = repo.getAll().find { it.sequence.SequenceId == mParam }!!
            _title.value = curr.sequence.title
            Log.d("my", "${curr.phases}")
            sets.value = curr.sequence.sets_number
            color.value = curr.sequence.color
            phasesList.value = (createViewData(curr.phases))
            Log.d("my2", "${title}")
            Log.d("my4", "${_title.value}")


        }
    }
    private fun createViewData(phaseList: List<PhaseModel>): List<ItemViewModel> {
        val viewData = mutableListOf<ItemViewModel>()
        phaseList.forEach {
            viewData.add(PhaseViewModel(it.title))
            }
        Log.d("my3", "${viewData}")

        return viewData
    }

    companion object {
        const val PHASE_ITEM = 0
        const val LISTING_ITEM = 1
        const val AD_ITEM = 2
    }

    fun onSplitTypeChanged(radioGroup: RadioGroup?, id: Int) {
        color.value = id
    }

    /*fun setTitle(_title: String){
        title.postValue( _title)
    }*/

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
        _title.postValue(s.toString())
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