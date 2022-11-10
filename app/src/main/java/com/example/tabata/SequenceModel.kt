package com.example.tabata

import androidx.annotation.ColorRes

data class SequenceModel (
    var title: String,
    @ColorRes var color: Int,
    var warm_up_time: Int,
    var sets_number: Int,
    var workout_time:Int,
    var break_time: Int,
    var repetitions_number: Int,
    var long_break_time: Int,
    ){


}