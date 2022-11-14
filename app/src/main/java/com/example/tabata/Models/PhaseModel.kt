package com.example.tabata.Models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phases")
data class PhaseModel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "phase_id") val PhaseId: Int,
    @NonNull val sequenceId : Int,
    @NonNull var phaseType: PhaseType,
    @NonNull val title: String,
    @NonNull val duration: Int,
    @NonNull val order: Int
    )
