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

     suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
           // val playlist = DevByteNetwork.devbytes.getPlaylist()
            //database.getDao().insertAll(playlist.asDatabaseModel())
        }
    }


}
