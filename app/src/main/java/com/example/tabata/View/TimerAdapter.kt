package com.example.tabata.View

import com.example.tabata.R
import android.graphics.Color
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.tabata.Db.MyDb
import com.example.tabata.Db.Repo
import com.example.tabata.Models.PhaseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimerAdapter (
    private var context: TimerActivity,
    private var workId: Long,
) : RecyclerView.Adapter<TimerAdapter.TimerViewHolder>() {
    //private var dao = MyDb.getDb(context).getDao()
    private  var ints: List<PhaseModel> = emptyList()
    class TimerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {


        val btn : Button = view.findViewById(R.id.interval_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerAdapter.TimerViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.timer_item, parent, false)
        CoroutineScope(Dispatchers.IO).launch{
            val repo = Repo(MyDb.getDb(context))
            ints = repo.getPhases(workId)

        }

        return TimerAdapter.TimerViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int = ints.size


    override fun onBindViewHolder(holder: TimerAdapter.TimerViewHolder, position: Int) {

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val theme : Boolean = sharedPref.getBoolean("theme_switch_preference", false)
        val font : String? = sharedPref.getString("font_preference", "-1")

        if (position == context.curPos)
            holder.btn.setBackgroundColor(Color.parseColor("#011f73"))
        else
        {
            holder.btn.setBackgroundColor(Color.parseColor(
                if (theme) {"#5e5e5e"} else {"#FF6200EE" }
            ))
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