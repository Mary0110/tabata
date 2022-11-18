package com.example.tabata.Models

import androidx.room.*
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.SequenceModel


data class SequenceWithPhases(
    @Embedded val sequence: SequenceModel,
    @Relation(
        parentColumn = "sequence_id",
        entity = PhaseModel::class,
        entityColumn = "parent_id" ,

    )
    val phases: List<PhaseModel>

)
