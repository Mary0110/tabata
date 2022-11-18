package com.example.tabata.View

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.PhaseType
import com.example.tabata.Models.SequenceModel
import com.example.tabata.R

class PhaseRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<PhaseModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhaseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.phase_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var phaseItem = items[position]

        when(holder) {
            is PhaseViewHolder -> {
                holder.bind(items.get(position))
                holder.minusButton.setOnClickListener{
                    val dur_text = holder.duration.text.toString().toInt()
                    if (dur_text > 0)
                        holder.duration.text = (dur_text -1).toString()
                }
                holder.plusButton.setOnClickListener{
                    val dur_text = holder.duration.text.toString().toInt()

                    holder.duration.text = (dur_text + 1).toString()
                }

                holder.minusButton.setOnLongClickListener{
                    val dur_text = holder.duration.text.toString().toInt()
                    if (dur_text > 5)
                        holder.duration.text = (dur_text -5).toString()
                    return@setOnLongClickListener true
                }
                holder.plusButton.setOnLongClickListener{
                    val dur_text = holder.duration.text.toString().toInt()
                    if (dur_text < 600)
                    holder.duration.text = (dur_text + 5).toString()
                    return@setOnLongClickListener true
                }


            }
        }

    }


    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(phaseList: List<PhaseModel>){
        items = phaseList
    }

    class PhaseViewHolder
    constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){

        val title: TextView = itemView.findViewById(R.id.phase_title_text)
        val image: ImageView = itemView.findViewById(R.id.phaseImage)
        val duration : TextView = itemView.findViewById(R.id.duration)
        val minusButton: Button = itemView.findViewById(R.id.button)
        val plusButton: Button = itemView.findViewById(R.id.button2)

        fun bind(phaseModel: PhaseModel){
            var imgRes: Int = R.drawable.ic_baseline_directions_run_24
            if(phaseModel.phaseType == PhaseType.WORK)
                imgRes = R.drawable.ic_baseline_directions_run_24
            else if(phaseModel.phaseType == PhaseType.BREAK)
                imgRes = R.drawable.ic_baseline_chair_24
            else if(phaseModel.phaseType == PhaseType.WARM_UP)
                imgRes = R.drawable.ic_baseline_emoji_people_24
            else if(phaseModel.phaseType == PhaseType.LONG_BREAK)
                imgRes = R.drawable.ic_baseline_emoji_food_beverage_24
            title.text = phaseModel.title
            image.setImageResource(imgRes)
            duration.text = phaseModel.duration.toString()
        }
    }


}
