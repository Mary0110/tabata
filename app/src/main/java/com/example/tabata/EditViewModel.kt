package com.example.tabata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditViewModel: ViewModel() {
    var title = MutableLiveData<String>()
    var workout_time = MutableLiveData<Int>()
    var break_time = MutableLiveData<Int>()
    var sets = MutableLiveData<Int>()
    var warmup_time = MutableLiveData<Int>()
    var long_break_time = MutableLiveData<Int>()
    var color = MutableLiveData<Int>()
    var repetitions_num = MutableLiveData<Int>()

    init{
        color.value=(R.id.radioButton5)//def value
    }

    fun setTitle(_title: String){
        title.value = _title
    }
    fun setWork(_title: Int){
        workout_time.value = _title
    }
    fun setBreak(_title: Int){
        break_time.value = _title
    }
    fun setSets(_title: Int){
        sets.value = _title
    }
    fun setWarm(_title: Int){
        warmup_time.value = _title
    }
    fun setLong(_title: Int){
        long_break_time.value = _title
    }
    fun setColor(_title: Int){
        color.value = _title
    }
    fun setRep(_title: Int){
        repetitions_num.value = _title
    }
}