package com.example.tabata.View

import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import android.util.TypedValue
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tabata.Db.Dao
import com.example.tabata.Db.MyDb
import com.example.tabata.Db.Repo
import com.example.tabata.Models.PhaseModel
import com.example.tabata.Models.PhaseType
import com.example.tabata.R
import com.example.tabata.View.adapters.TimerAdapter
import com.example.tabata.services.TimerService
import kotlin.math.roundToInt
import com.example.tabata.databinding.ActivityTimerBinding
import com.example.tabata.viewModel.TimerViewModel
import kotlinx.coroutines.launch


class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding
    lateinit var adapter: TimerAdapter
    private var timerStarted = false
    lateinit var viewModel: TimerViewModel

    var time = 0.0
    var workId : Long = -1

    var mxPos : Int = 1
    var curPos : Int = 0
    var dao : Dao? = null
    var phases : MutableList<PhaseModel> = ArrayList()
    var sound: Boolean? = null

    companion object {
        const val CHANNEL_ID : String = "123123"
        const val NOTIFICATION_ID : Int = 7894613
    }

    lateinit var timerService: TimerService
    var isBound = false

    private val connection = object : ServiceConnection {

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.TimerBinder
            timerService = binder.getService()
            timerService.context = this@TimerActivity
            isBound = true
            Log.d("myser", "wqq")

            if (timerService.isRunning == false) {
                this@TimerActivity.foregroundStartService("Start")            }

            if (timerService.id == (-1).toLong())
                timerService.id = workId
            else
            {
                workId = timerService.id
                curPos = timerService.curPos
            }
            val repo = Repo(MyDb.getDb(application))

            lifecycleScope.launch{
                phases = repo.getPhases(workId).toMutableList()
                sound = repo.getSequence(workId).SoundEffect
                binding.intervalsRecyclerView.layoutManager = LinearLayoutManager(this@TimerActivity)
                adapter = TimerAdapter(this@TimerActivity, workId)
                if(phases!!.isNotEmpty())
                    adapter.submitList(phases)
                Log.d("myphases", "$phases")
                binding.intervalsRecyclerView.adapter = adapter/*TimerAdapter(this@TimerActivity, workId)*/

                timerService.isRunning = true

            }

            registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

            binding.nextIntervalButton.setOnClickListener { nxtTimerInterval() }
            binding.prevIntervalButton.setOnClickListener { prevTimerInterval() }
            binding.stopStartTimeButton.setOnClickListener { stopStartTimer() }


            binding.stopStartTimeButton.icon = AppCompatResources.getDrawable(this@TimerActivity, R.drawable.ic_baseline_pause_24)
            timerStarted = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        isBound = true
        Log.d("myonstart", "$isBound")
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        isBound = false
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras

        bundle?.let {
            bundle.apply {
                Log.d("myid","$workId")
                workId = getLong("id")
                Log.d("myid","$workId")
            }
        }

        updateTheme()
    }

    private fun stopStartTimer() {
        if(timerStarted)
            stopTimer()
        else
            startTimer()
    }

    private fun startTimer() {
        timerService.isRunning = true

        binding.stopStartTimeButton.icon = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_pause_24)
        timerStarted = true
    }

    private fun stopTimer() {
        timerService.isRunning = false

        binding.stopStartTimeButton.icon = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_play_arrow_24)
        timerStarted = false
    }

    private fun prevTimerInterval() {
        if (curPos != 0) {
            curPos--
            timerService.curPos--
            timerService.Gtime = phases!![curPos].duration.toDouble()

            binding.intervalsRecyclerView.adapter?.notifyItemChanged(curPos)
            binding.intervalsRecyclerView.adapter?.notifyItemChanged(curPos + 1)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun nxtTimerInterval() {
        if (curPos < mxPos) {


            curPos++

            if (curPos == mxPos) {
                timerService.isRunning = false
                this@TimerActivity.foregroundStartService("Exit")
//                timerService.stopForeground(Service.STOP_FOREGROUND_REMOVE)


                val setIntent = Intent (this@TimerActivity, MainActivity::class.java)

                this@TimerActivity.startActivity(setIntent)

                finish()

            }
            else {

                timerService.curPos++
                timerService.Gtime = phases!![curPos].duration.toDouble()
                binding.intervalsRecyclerView.adapter?.notifyItemChanged(curPos)
                binding.intervalsRecyclerView.adapter?.notifyItemChanged(curPos - 1)
            }
        }
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {

        private lateinit var statusReceiver: BroadcastReceiver
        private lateinit var timeReceiver: BroadcastReceiver

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onReceive(context: Context?, intent: Intent?) {

            val id : Int = intent!!.getIntExtra(TimerService.ACTION_ID_EXTRA, 0)
            curPos = intent.getIntExtra(TimerService.CURRENT_POSITION_EXTRA, 0)
            val nameId : Int = intent.getIntExtra(TimerService.NAME_ID_EXTRA, 0)
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            mxPos = intent.getIntExtra(TimerService.MAX_POSITION_EXTRA, 1)
            val tLeft = intent.getStringExtra(TimerService.OV_TIME_LEFT_EXTRA)

            val name : String = if (nameId < 5) { PhaseType.values()[nameId].name } else {"Финиш"}

            if (id == 1) {
                binding.intervalTimeLeftTextView.text = getTimeStringFromDouble(time)
                binding.intervalsCountTextView.text = String.format("%02d/%02d", curPos + 1, mxPos)
                binding.overallLeftTimeTextView.text = tLeft
                binding.currentIntervalNameTextView.text = name
            }
            else if (id == 2) {

                if (curPos == mxPos) {

                    timerService.isRunning = false
                    this@TimerActivity.foregroundStartService("Exit")

                    val setIntent = Intent (this@TimerActivity, MainActivity::class.java)

                    this@TimerActivity.startActivity(setIntent)

                    finish()
                }
                else {

                    binding.intervalsRecyclerView.adapter?.notifyItemChanged(curPos)
                    binding.intervalsRecyclerView.adapter?.notifyItemChanged(curPos - 1)
                    binding.intervalTimeLeftTextView.text = getTimeStringFromDouble(time)
                    binding.intervalsCountTextView.text = String.format("%02d/%02d", curPos + 1, mxPos)
                    binding.overallLeftTimeTextView.text = tLeft
                    binding.currentIntervalNameTextView.text = name
                }
            }

        }
    }

    fun pressBtn(pos : Int) {

        if (curPos == pos) return

        binding.intervalsRecyclerView.adapter?.notifyItemChanged(curPos)
        binding.intervalsRecyclerView.adapter?.notifyItemChanged(pos)

        curPos = pos
        timerService.curPos = pos
        timerService.Gtime = phases!![curPos].duration.toDouble()
    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {

        timerService.isRunning = false
        this@TimerActivity.foregroundStartService("Exit")
//        timerService.stopForeground(Service.STOP_FOREGROUND_REMOVE)

        val setIntent = Intent (this, MainActivity::class.java)

        this.startActivity(setIntent)

        finish()
    }

    fun updateTheme() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val theme : Boolean = sharedPref.getBoolean("theme_switch_preference", false)
        Log.d("mypref", "$theme")
        val bcg : ConstraintLayout = findViewById(R.id.action_constraint_layout)
        val font : String? = sharedPref.getString("font_preference", "-1")


        if (font == "1") {
            binding.currentIntervalNameTextView.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                binding.currentIntervalNameTextView.textSize * (0.5).toFloat()
            )
        }
        if (font == "3")
            binding.currentIntervalNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.currentIntervalNameTextView.textSize * (1.5).toFloat())


        if(theme) {

            bcg.setBackgroundColor(baseContext.resources.getColor(R.color.darktheme))

        } else
        {
            bcg.setBackgroundColor(baseContext.resources.getColor(R.color.white))

        }
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String = String.format("%02d", seconds)
}

@RequiresApi(Build.VERSION_CODES.O)
fun Context.foregroundStartService(command: String) {
    val intent : Intent = Intent(this, TimerService::class.java)

    intent.putExtra("command", command)
    this.startForegroundService(intent)
}
