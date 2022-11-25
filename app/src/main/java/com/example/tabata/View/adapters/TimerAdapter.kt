package com.example.tabata.View.adapters

import com.example.tabata.R
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.tabata.Models.PhaseModel
import com.example.tabata.View.TimerActivity

class TimerAdapter (
    private var context: TimerActivity,
    private var workId: Long,
) : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {
    private  var ints: MutableList<PhaseModel> = ArrayList()

    class TimerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val btn : Button = view.findViewById(R.id.interval_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.timer_item, parent, false)
        /*CoroutineScope.launch{
            val repo = Repo(MyDb.getDb(context))
            ints = repo.getPhases(workId)
            Log.d("phases", "$ints")

        }*/

        return TimerViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int = ints.size

    fun submitList(seqList: MutableList<PhaseModel>){
        ints.run {
            clear()
            addAll(seqList)
        }
        //items = seqList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val theme : Boolean = sharedPref.getBoolean("theme_switch_preference", false)
        val font : String? = sharedPref.getString("font_preference", "-1")

        if (position == context.curPos)
            holder.btn.setBackgroundColor(context.resources.getColor(R.color.teal_700))
        else
        {
            holder.btn.setBackgroundColor(context.resources.getColor(R.color.black))
        }

        holder.btn.text = String.format(
            "%02d. %s",
            position + 1,
            context.resources.getStringArray(R.array.interval_types)[ints[position].phaseType.ordinal + 1]
        )

        holder.btn.setOnClickListener { context.pressBtn(position) }

        if (font == "1") {
            holder.btn.setTextSize(TypedValue.COMPLEX_UNIT_PX,  (52 * 0.5).toFloat())
        }


        if (font == "3") {
            holder.btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, (52 * 1.4).toFloat())
        }
    }
}