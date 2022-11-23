package com.example.tabata.Db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.SequenceModel
import com.example.tabata.Models.SequenceWithPhases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext

class Repo(private val database: MyDb) {


suspend fun getAll(): List<SequenceWithPhases>{
    val data: List<SequenceWithPhases>
    withContext(Dispatchers.IO) {
         data = database.getDao().getAllSequencesWithPhases()
        Log.d("myrepo", "$data")

    }
    return data
}
    suspend fun getLastSeq(): Long{
        val id : Long

        withContext(Dispatchers.IO) {
            id = database.getDao().getMaxSequenceId()
        }
        return  id
    }

    suspend fun getPhases(id: Long): List<PhaseModel> {
        val data: List<PhaseModel>

        withContext(Dispatchers.IO) {
            data = database.getDao().getPhases(id)
        }
        return data
    }

    fun getSequences(): LiveData<List<SequenceModel>> = database.getDao().getAllSequences()

    suspend fun removeSequence(s:SequenceModel){
        withContext(Dispatchers.IO) {
             database.getDao().deleteSequence(s)
        }
    }
    suspend fun removePhase(s:PhaseModel){
        withContext(Dispatchers.IO) {
            database.getDao().deletePhase(s)
        }
    }

   // val phases_of_seq : Flow<List<PhaseModel>> = database.getDao().getPhases(seqId)
   /* fun getById(id:Int):SequenceWithPhases?
   {
       var swph:SequenceWithPhases = data.value
       for(s in data)
       {
           if(s.sequence.sequenceId == id)
           {
             swph = s
           }
       }
       return swph
    }*/
    suspend fun updateSequence(seq: SequenceModel) {
       withContext(Dispatchers.IO) {
           database.getDao().updateSequence(seq)
       }

    }
    suspend fun insertPhases(list:List<PhaseModel>){
       withContext(Dispatchers.IO) {
           database.getDao().insertPhase(*list.toTypedArray())

       }
    }
       suspend fun insertSequence(seq: SequenceModel){
           withContext(Dispatchers.IO) {
               database.getDao().insertSequence(seq)
           }}
suspend fun addBoth(s:SequenceModel, l:List<PhaseModel>){
    withContext(Dispatchers.IO){
        database.getDao().addBoth(s,l)
    }

}

   suspend fun removePhaseById(id: Long) {
       withContext(Dispatchers.IO) {
           database.getDao().deletePhaseById(id)
       }
   }

    suspend fun getLastPhase(): Long {
        val id : Long

        withContext(Dispatchers.IO) {
            id = database.getDao().getMaxPhaseId()
        }
        return  id
    }

    /*suspend fun refreshVideos() {
       withContext(Dispatchers.IO) {
           val playlist = DevByteNetwork.devbytes.getPlaylist()
           //database.getDao().insertAll(playlist.asDatabaseModel())
       }
   }*/


}
