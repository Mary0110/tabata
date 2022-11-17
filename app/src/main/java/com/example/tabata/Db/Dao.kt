package com.example.tabata.Db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.SequenceModel
import com.example.tabata.Models.SequenceWithPhases

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhase(vararg phase: PhaseModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSequence(seq: SequenceModel)

    @Query("SELECT * FROM sequences")
    fun getAllSequences(): List<SequenceModel>

    @Query("SELECT * FROM phases WHERE :seqId = sequenceId ORDER BY `order`  ")
    fun getPhases(seqId:Int): List<PhaseModel>

    @Query("SELECT * FROM sequences WHERE :seqId = sequence_id ")
    fun getSequence(seqId:Int): SequenceModel

    @Update
    fun updatePhase(phase: PhaseModel)

    @Update
    fun updateSequence(seq: SequenceModel)

    @Transaction
    @Query("SELECT * FROM sequences")
    fun getAllSequencesWithPhases():List<SequenceWithPhases>

    @Transaction
    @Query("SELECT * FROM sequences")
    fun getAllSequenceWithPhases():SequenceWithPhases

    @Query("DELETE FROM sequences")
    fun deleteAllSequences()

    @Query("DELETE FROM phases")
    fun deleteAllPhases()

    @Query("SELECT MAX(sequence_id) FROM sequences")
    fun getMaxSequenceId() : Int


    @Query("SELECT MAX(phase_id) FROM phases")
    fun getMaxPhaseId() : Int
}