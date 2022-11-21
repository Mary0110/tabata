package com.example.tabata.Models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phases")
data class PhaseModel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "phase_id") val phaseId: Long? = null,
    @ColumnInfo(name = "parent_id") var sequenceId : Long?,
    @NonNull var phaseType: PhaseType,
    @NonNull val title: String = "default_title",
    @NonNull val duration: Int = 1,
    @NonNull val order: Int)
