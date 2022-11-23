package com.example.tabata.View

import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.tabata.services.TimerService
import kotlin.math.roundToInt
import com.example.tabata.databinding.ActivityTimerBinding
import kotlinx.coroutines.launch


class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding
    private var timerStarted = false

    var time = 0.0
    var workId : Long = -1

    var mxPos : Int = 1
    var curPos : Int = 0
    var dao : Dao? = null
    var phases : MutableList<PhaseModel>? = null

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

           // dao = MyDb.getDb(application).getDao()
            lifecycleScope.launch{
                phases = repo.getPhases(workId).toMutableList()
            }
                    //dao?.getPhases(workId)

            timerService.isRunning = true

            registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

            binding.nextIntervalButton.setOnClickListener { nxtTimerInterval() }
            binding.prevIntervalButton.setOnClickListener { prevTimerInterval() }
            binding.stopStartTimeButton.setOnClickListener { stopStartTimer() }

            binding.intervalsRecyclerView.layoutManager = LinearLayoutManager(this@TimerActivity)
            binding.intervalsRecyclerView.adapter = TimerAdapter(this@TimerActivity, workId)

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

        //updateTheme()
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
//                    timerService.stopForeground(Service.STOP_FOREGROUND_REMOVE)


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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun updateTheme() {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val theme : Boolean = sharedPref.getBoolean("theme_switch_preference", false)
        Log.d("mypref", "$theme")
        val bcg : ConstraintLayout = findViewById(R.id.action_constraint_layout)
        val font : String? = sharedPref.getString("font_preference", "-1")


        if (font == "1")
            binding.currentIntervalNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.currentIntervalNameTextView.textSize * (0.5).toFloat())

        if (font == "3")
            binding.currentIntervalNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, binding.currentIntervalNameTextView.textSize * (1.5).toFloat())

        binding.prevIntervalButton.setBackgroundColor(Color.parseColor("#5e5e5e"))

        if(theme) {

            bcg.setBackgroundColor(Color.parseColor("#5e5e5e"))
            val col : ColorDrawable = ColorDrawable(Color.parseColor("#000000"))
            getSupportActionBar()?.setBackgroundDrawable(col)
            binding.prevIntervalButton.setBackgroundColor(Color.parseColor("#5e5e5e"))
            binding.nextIntervalButton.setBackgroundColor(Color.parseColor("#5e5e5e"))
            binding.stopStartTimeButton.setBackgroundColor(Color.parseColor("#5e5e5e"))

        } else {
            bcg.setBackgroundColor(Color.parseColor("#FF6200EE"))
            val col : ColorDrawable = ColorDrawable(Color.parseColor("#FF6200EE"))
            getSupportActionBar()?.setBackgroundDrawable(col)

            binding.prevIntervalButton.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.nextIntervalButton.setBackgroundColor(Color.parseColor("#FF6200EE"))
            binding.stopStartTimeButton.setBackgroundColor(Color.parseColor("#FF6200EE"))

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
