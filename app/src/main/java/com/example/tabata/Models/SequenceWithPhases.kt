package com.example.tabata.Models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.SequenceModel


data class SequenceWithPhases(
    @Embedded val sequence: SequenceModel,
    @Relation(
        parentColumn = "sequence_id",
        entityColumn = "phase_id"
    )
    val phases: List<PhaseModel>
)
