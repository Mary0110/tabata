package com.example.tabata.viewModel

import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tabata.Models.PhaseModel
import com.example.tabata.R


class EditViewModel: ViewModel() {
    var phasesList = MutableLiveData<List<PhaseModel>>()
    var title = MutableLiveData<String>()
    var sets = MutableLiveData<Int>()
    var color = MutableLiveData<Int>()

    init{
        title.value="default title"//def value

        sets.value=0//def value

        color.value=(R.id.radioButton4)//def value

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
}