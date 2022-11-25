package com.example.tabata.View.adapters



import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tabata.Models.SequenceModel
import com.example.tabata.R
import com.example.tabata.viewModel.EditViewModel


class SequenceRecyclerAdapter(val listener: ClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    //private val TAG: String = "AppDebug"

    private var items: MutableList<SequenceModel> = ArrayList()

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


                //holder.card.setBackgroundColor(Color.parseColor("#FFBB86FC"))
            }
        }

    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(seqList: MutableList<SequenceModel>){
        items.run {
            clear()
            addAll(seqList)
        }
        //items = seqList.toMutableList()
        notifyDataSetChanged()
    }

    class SequenceViewHolder
    constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){

        val title: TextView = itemView.findViewById(R.id.title_text)
        val card : ConstraintLayout = itemView.findViewById(R.id.card)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(itemView.context)
        val font : String? = sharedPref.getString("font_preference", "-1")


        fun bind(sequenceModel: SequenceModel, listener: ClickListener){
            title.text = sequenceModel.title
            if (font == "1") {
                title.setTextSize(TypedValue.COMPLEX_UNIT_PX,  (80).toFloat())
            }
            else if (font == "3") {
                title.setTextSize(TypedValue.COMPLEX_UNIT_PX,  (150).toFloat())
            }


            var backgroundColor = itemView.context.resources.getColor(R.color.mygreen)

            if(sequenceModel.color == R.id.radioButton3)
                 backgroundColor = itemView.context.resources.getColor(R.color.myred)
            else if(sequenceModel.color ==  R.id.radioButton4)
                backgroundColor = itemView.context.resources.getColor(R.color.myyellow)
            else if(sequenceModel.color ==  R.id.radioButton5)
                backgroundColor = itemView.context.resources.getColor(R.color.mygreen)
            card.setBackgroundColor((backgroundColor))
            Log.d("ifstat", "${backgroundColor}")

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


