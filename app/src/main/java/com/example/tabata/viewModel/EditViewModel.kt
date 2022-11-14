package com.example.tabata.viewModel

import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tabata.R


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
        title.value="default title"//def value
        workout_time.value=0//def value
        break_time.value=(0)//def value
        sets.value=0//def value
        warmup_time.value=0
        long_break_time.value=0
        color.value=(R.id.radioButton4)//def value
        repetitions_num.value=0
    }

    fun onSplitTypeChanged(radioGroup: RadioGroup?, id: Int) {
        color.value = id
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

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        title.postValue(s.toString())
        Log.w("tag", "onTextChanged $s")
    }
}