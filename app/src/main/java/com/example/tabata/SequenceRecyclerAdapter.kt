package com.example.tabata



import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class SequenceRecyclerAdapter(val listener: ClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    //var selectedPos = RecyclerView.NO_POSITION
    //private val TAG: String = "AppDebug"

    private var items: List<SequenceModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SequenceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.sequence_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SequenceViewHolder -> {
                holder.bind(items.get(position), listener)
            }
        }

    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(seqList: List<SequenceModel>){
        items = seqList
    }

    class SequenceViewHolder
    constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){

        val title: TextView = itemView.findViewById(R.id.title_text)
        val workout_time: TextView = itemView.findViewById(R.id.workout_time)
        val break_time: TextView = itemView.findViewById(R.id.break_time)
        val repetitions: TextView = itemView.findViewById(R.id.repetitions_num)
        val warmup: TextView = itemView.findViewById(R.id.warmup_time)
        val sets: TextView = itemView.findViewById(R.id.sets_num)
        val long_break: TextView = itemView.findViewById(R.id.long_break_time)


        fun bind(sequenceModel: SequenceModel, listener: ClickListener){
            title.text = sequenceModel.title
            workout_time.text = sequenceModel.workout_time.toString()
            break_time.text = sequenceModel.break_time.toString()
            repetitions.text = sequenceModel.repetitions_number.toString()
            warmup.text = sequenceModel.warm_up_time.toString()
            sets.text = sequenceModel.sets_number.toString()
            long_break.text = sequenceModel.long_break_time.toString()

            itemView.setOnClickListener{
                listener.onClick(sequenceModel)
            }
            itemView.setOnLongClickListener {
                listener.onLongClick(sequenceModel)
                return@setOnLongClickListener true
            }
        }
    }
    interface ClickListener{
        fun onClick(sequence: SequenceModel)
        fun onLongClick(sequence: SequenceModel)
    }
}


