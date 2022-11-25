package com.example.tabata.viewModel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.tabata.Db.MyDb
import com.example.tabata.Db.Repo
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.SequenceModel
import com.example.tabata.Models.SequenceWithPhases
import com.example.tabata.R
import kotlinx.coroutines.launch


class EditViewModel( application: Application, val mParam: Long):
    AndroidViewModel(application) {

    val zero:Long = 0
    var phasesList = MutableLiveData<List<PhaseViewModel>>(emptyList())
    val db:MyDb
    private var _title = MutableLiveData<String>()
    var sets = MutableLiveData<Int>()
    var sound = MutableLiveData<Boolean>()

    var color = MutableLiveData<Int>()
        var theme: Boolean = false
    lateinit var curr: SequenceWithPhases
    var repo : Repo
    val data: LiveData<List<PhaseViewModel>>
        get() = phasesList
    val title: LiveData<String>
        get() = _title

     var pref : SharedPreferences
     var font: String

    init {
       pref = PreferenceManager.getDefaultSharedPreferences(application)
       font  = pref.getString("font_preference", "-1").toString()
       theme = pref.getBoolean("theme_switch_preference", false)

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


    fun getLastAddedId(): Long{
        var id: Long = -1
        viewModelScope.launch {
           id =  repo.getLastPhase()
        }
        return id
    }

    fun getThemeColor(): Int{
        return if (theme)
            getApplication<Application?>().applicationContext.resources.getColor(R.color.darktheme)
        else
            getApplication<Application?>().applicationContext.resources.getColor(R.color.white)
    }

    private fun loadData(application: Application, mParam:Long) {

        viewModelScope.launch {
            curr = repo.getAll().find { it.sequence.SequenceId == mParam }!!
            _title.value = curr.sequence.title
            sets.value = curr.sequence.sets_number
            color.value = curr.sequence.color
            phasesList.value = (createViewData(curr.phases))
            sound.value = curr.sequence.SoundEffect
        }
    }
    private fun createViewData(phaseList: List<PhaseModel>): List<PhaseViewModel> {
        val viewData = mutableListOf<PhaseViewModel>()
        phaseList.forEach {
            viewData.add(PhaseViewModel(it.title, it.duration, it.phaseType, it.phaseId!!))
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

    fun saveSequence(){

        var titleToPass= "default title"
        if(_title.value  != null)
             titleToPass = _title.value!!


        viewModelScope.launch {
            if(phasesList.value == null)
            {
                if(mParam== zero)
                {
                    var seq = SequenceModel(null, titleToPass, color.value!!, sets.value!!, sound.value!!)
                    viewModelScope.launch { repo.insertSequence(seq) }
                }
                else{
                    var seq = SequenceModel(mParam, titleToPass, color.value!!, sets.value!!,  sound.value!!)
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
                    var seq = SequenceModel(null, titleToPass, color.value!!, sets.value!!,  sound.value!!)
                    viewModelScope.launch { repo.addBoth(seq, list.toList()) }

                }
                else{
                    var list: MutableList<PhaseModel> = mutableListOf()
                    phasesList.value!!.forEachIndexed{ind, item ->
                        var phase = PhaseModel(sequenceId = mParam,
                            phaseType = item.type,
                            title = item.title,
                            duration = item.duration,
                            order=ind + 1)
                        list.add(phase)
                    }
                    var seq = SequenceModel(mParam, titleToPass, color.value!!, sets.value!!,  sound.value!!)
                    viewModelScope.launch {
                        repo.addBoth(seq, list.toList())
                    }
                }
            }
        }
    }

    fun onClickDelete(id: Long) {
        Log.d("mydel","")

        if(phasesList.value != null){
            val toDel = phasesList.value!!.find{
                it.phaseId == id
            }
            var old =  phasesList.value!!.toMutableList()
            old.remove(toDel)
            phasesList.value = old.toList()
            viewModelScope.launch {
                repo.removePhaseById(id)
            }
        }
    }

    fun onCheckedChanged(checked: Boolean) {
        sound.value = checked
    }
    }
