package com.example.tabata.View



import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tabata.Models.SequenceModel
import com.example.tabata.R
import java.security.AccessController.getContext


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
        var seqItem = items[position]

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
       // val card : ConstraintLayout = itemView.findViewById(R.id.card)

        fun bind(sequenceModel: SequenceModel, listener: ClickListener){
            title.text = sequenceModel.title
           // card.setBackgroundColor(Color.parseColor("#FFEB3B"))

            //var backgroundColor = Color.parseColor("#FFEB3B")
//
//            if(sequenceModel.color == R.id.radioButton3)
//                backgroundColor = Color.parseColor("#4CAF50")
//            else if(sequenceModel.color ==  R.id.radioButton4)
//                backgroundColor = Color.parseColor("#FFEB3B")
//            else if(sequenceModel.color ==  R.id.radioButton5)
//                backgroundColor = Color.parseColor("#F44336")
            Log.d("ifstat", "inside")

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


