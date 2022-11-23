package com.example.tabata.viewModel

import android.util.Log
import androidx.annotation.LayoutRes
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged
import com.example.tabata.Models.PhaseType
import com.example.tabata.R
import com.example.tabata.BR

import com.example.tabata.viewModel.EditViewModel

interface ItemViewModel {
    @get:LayoutRes
    val layoutId: Int
    val viewType: Int
        get() = 0
}

class PhaseViewModel( @get:Bindable var title: String = "default title",
                     @get:Bindable var duration: Int = 10,
                     val type: PhaseType, val phaseId: Long)
    : BaseObservable(), ItemViewModel {

    override val layoutId: Int = R.layout.phase_row
    override val viewType: Int = EditViewModel.PHASE_ITEM

    var imageResource : Int = 0
        get() = setIcon()

    fun onClickPlus() {
        if(duration< 1000)
            duration += 1
        notifyPropertyChanged(BR.duration)

    }

    fun onClickMinus(){
        if(duration> 1)
            duration -= 1
        notifyPropertyChanged(BR.duration)
    }

    fun setIcon(): Int{
        var resource = R.drawable.ic_baseline_directions_run_24
        if(type == PhaseType.BREAK)
            resource = R.drawable.ic_baseline_chair_24
        else if(type == PhaseType.WORK)
            resource=  R.drawable.ic_baseline_directions_run_24
        else if(type == PhaseType.WARM_UP)
            resource =  R.drawable.ic_baseline_emoji_people_24
        else if(type == PhaseType.LONG_BREAK)
            resource = R.drawable.ic_baseline_emoji_food_beverage_24
        return resource
    }

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        title = s.toString()
        notifyPropertyChanged(BR.title)

    }

    fun onClickDelete()
    {
        Log.d("njhdscn", "")

    }
}