package com.example.tabata.Models

import androidx.annotation.IdRes
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tabata.R

@Entity(tableName = "sequences")
data class SequenceModel (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "sequence_id")  val SequenceId: Int?,
    @NonNull var title: String,
    @NonNull @IdRes var color: Int = R.id.radioButton3,
    @NonNull var sets_number: Int = 1,
    @NonNull @ColumnInfo(name = "sound_effect") val SoundEffect: Boolean

    ){


}