package com.example.tabata.viewModel

import android.app.Application
import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.*
import androidx.room.withTransaction
import com.example.tabata.Db.MyDb
import com.example.tabata.Db.Repo
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.SequenceModel
import com.example.tabata.Models.SequenceWithPhases
import com.example.tabata.R
import kotlinx.coroutines.launch


class EditViewModel(application: Application, val mParam: Long):
    AndroidViewModel(application) {
        val zero:Long = 0
    var phasesList = MutableLiveData<List<PhaseViewModel>>(emptyList())
    val db:MyDb
    private var _title = MutableLiveData<String>()
    var sets = MutableLiveData<Int>()
    var color = MutableLiveData<Int>()
    lateinit var curr: SequenceWithPhases
    lateinit var repo : Repo
    val data: LiveData<List<PhaseViewModel>>
        get() = phasesList
    val title: LiveData<String>
        get() = _title
   // var data: LiveData<List<SequenceWithPhases>>
    init{
        db = MyDb.getDb(application)
       repo = Repo(db)
       if(mParam!= zero) {
           loadData(application, mParam)
       }
       else
       {
           _title.value = ""
            sets.value = 1
            color.value = R.id.radioButton5
       }
       Log.d("myinitialized", "init")

    }

    private fun loadData(application: Application, mParam:Long) {

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
    private fun createViewData(phaseList: List<PhaseModel>): List<PhaseViewModel> {
        val viewData = mutableListOf<PhaseViewModel>()
        phaseList.forEach {
            viewData.add(PhaseViewModel(it.title, it.duration, it.phaseType))
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

    /*fun createPhaseModelList(): List<PhaseModel>
    {
        var list : List<PhaseModel> = emptyList()
        //phasesList.value?.forEach{
            //PhaseModel(null,
          //  )
        //}

    }*/

    fun saveSequence(){

        var titleToPass= "default title"
        if(_title.value  != null)
             titleToPass = _title.value!!


        viewModelScope.launch {
            if(phasesList.value == null)
            {
                if(mParam== zero)
                {
                    var seq = SequenceModel(null, titleToPass, color.value!!, sets.value!!, true)
                    viewModelScope.launch { repo.insertSequence(seq) }
                }
                else{
                    var seq = SequenceModel(mParam, titleToPass, color.value!!, sets.value!!, true)
                    viewModelScope.launch { repo.updateSequence(seq) }
                }
            }
            else{
                if(mParam==zero){
                    var list: MutableList<PhaseModel> = mutableListOf()
                    phasesList.value!!.forEachIndexed{ind, item ->
                        var phase = PhaseModel(sequenceId = null,
                            phaseType = item.type,
                            title = item.title,
                            duration = item.duration,
                            order=ind + 1)
                        list.add(phase)
                    }
                    var seq = SequenceModel(null, titleToPass, color.value!!, sets.value!!, true)
                    viewModelScope.launch { repo.addBoth(seq, list.toList()) }

                }
                else{
                    var list: MutableList<PhaseModel> = mutableListOf()
                    phasesList.value!!.forEachIndexed{ind, item ->
                        var phase = PhaseModel(sequenceId = null,
                            phaseType = item.type,
                            title = item.title,
                            duration = item.duration,
                            order=ind + 1)
                        list.add(phase)
                    }
                    var seq = SequenceModel(mParam, titleToPass, color.value!!, sets.value!!, true)
                    viewModelScope.launch { repo.addBoth(seq, list.toList()) }


                }
            }
        }
    }
}
//    fun savePhases(){
//        if(phasesList.value != null)
//        {
//            var lastSeq: Int = 0
//            viewModelScope.launch {
//                   lastSeq = repo.getLastSeq()
//            }
//            Log.d("last", "$lastSeq")
//
//            viewModelScope.launch {
//            }
//
//        }
//    }
//}


