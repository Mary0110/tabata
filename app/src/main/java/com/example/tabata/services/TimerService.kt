package com.example.tabata.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.tabata.Db.Dao
import com.example.tabata.Db.MyDb
import com.example.tabata.Models.PhaseModel
import com.example.tabata.R
import com.example.tabata.View.TimerActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


const val INTENT_COMMAND = "Command"
const val INTENT_COMMAND_BACK = "Back"
const val INTENT_COMMAND_FORWARD = "Forward"
const val INTENT_COMMAND_RESUME = "Resume"
const val INTENT_COMMAND_PAUSE = "Pause"
const val INTENT_COMMAND_START = "Start"
const val INTENT_COMMAND_EXIT = "Exit"

private const val NOTIFICATION_CHANNEL_GENERAL = "Checking"
private const val CODE_FOREGROUND_SERVICE = 1
const val CODE_BACK_INTENT = 2
const val CODE_FORWARD_INTENT = 3
const val CODE_RESUME_INTENT = 4
const val CODE_PAUSE_INTENT = 5
private const val CODE_CLICK_INTENT = 6

class TimerService : Service() {

    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    private val binder = TimerBinder()
    lateinit var context: TimerActivity
    override fun onBind(intent: Intent?): IBinder = binder

    val timer : Timer = Timer()
    var id : Long = -1

    var dao : Dao? = null
    var phases : MutableList<PhaseModel>? = null
    var curPos : Int = -1
    var Gtime = -1.0
    var isRunning = false

    private lateinit var tmpIntent : Intent

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("UnspecifiedImmutableFlag")
    fun clickPendingIntent(): PendingIntent? {
        val clickIntent = Intent(
            this,
            TimerActivity::class.java
        )

        clickIntent.putExtra("id", id)

        return androidx.core.app.TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(CODE_CLICK_INTENT, PendingIntent.FLAG_IMMUTABLE)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun backPendingIntent(): PendingIntent {
        val backIntent = Intent(this, TimerService::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_BACK)
        }
        return PendingIntent.getService(
            this,
            CODE_BACK_INTENT,
            backIntent,
            0
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun forwardPendingIntent(): PendingIntent {
        val forwardIntent = Intent(this, TimerService::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_FORWARD)
        }
        return PendingIntent.getService(
            this,
            CODE_FORWARD_INTENT,
            forwardIntent,
            0
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun pausePendingIntent(): PendingIntent {
        val pauseIntent = Intent(this, TimerService::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_PAUSE)
        }
        return PendingIntent.getService(
            this,
            CODE_PAUSE_INTENT,
            pauseIntent,
            0
        )
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun resumePendingIntent(): PendingIntent {
        val resumeIntent = Intent(this, TimerService::class.java).apply {
            putExtra(INTENT_COMMAND, INTENT_COMMAND_RESUME)
        }
        return PendingIntent.getService(
            this,
            CODE_RESUME_INTENT,
            resumeIntent,
            0
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun notifyBind() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        with(
            NotificationChannel(
                TimerActivity.CHANNEL_ID,
                "Timer",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        ) {
            enableLights(false)
            setShowBadge(false)
            enableVibration(false)
            setSound(null, null)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            manager.createNotificationChannel(this)
        }

        with(
            NotificationCompat.Builder(this, TimerActivity.CHANNEL_ID)
        ) {
            setTicker(null)
            setContentTitle(Gtime.toInt().toString())
            setContentText(context.resources.getStringArray(R.array.interval_types)[phases!![curPos].phaseType.ordinal + 1])
            setAutoCancel(false)
            setOngoing(true)
            setWhen(System.currentTimeMillis())
            setSmallIcon(R.drawable.ic_launcher_foreground)
            priority = Notification.PRIORITY_MAX
            setContentIntent(clickPendingIntent())
            startForeground(CODE_FOREGROUND_SERVICE, build())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val command = intent?.getStringExtra("command")
        if (command == "Exit") {
            stopSelf()
            return START_NOT_STICKY
        }

        if (command == "Stop") {
            isRunning = false
        }

        if (command == "Start") {

            if (dao == null) {
                id = context.workId
                CoroutineScope(Dispatchers.IO).launch {
                    dao = MyDb.getDb(application).getDao()
                    phases = dao?.getPhases(id)
                    Log.d("mycoroutine", "$phases")
                    curPos = 0
                    val time = phases!![curPos].duration.toDouble()
                    Gtime = time
                    timer.scheduleAtFixedRate(TimeTask(Gtime), 0, 1000)
                    isRunning = true
                }


            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy()
    {
        timer.cancel()
        super.onDestroy()
    }

    private inner class TimeTask(private var time: Double) : TimerTask()
    {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {

            if (!isRunning) {
                notifyBind()
                return
            }

            time = Gtime

            time--
            val intent = Intent(TIMER_UPDATED)

            if (time <= 0) {
                curPos++

                if (curPos < phases?.size!!) {
                    val sndTimer = Timer()
                    sndTimer.scheduleAtFixedRate(SoundTask(1, RingtoneManager.getRingtone(applicationContext, RingtoneManager.getDefaultUri(
                        RingtoneManager.TYPE_RINGTONE))), 0, 1000)
                }
                else {
                    val sndTimer = Timer()
                    sndTimer.scheduleAtFixedRate(SoundTask(1, RingtoneManager.getRingtone(applicationContext, RingtoneManager.getDefaultUri(
                        RingtoneManager.TYPE_ALARM))), 0, 2000)
                }

                time = if (curPos < phases?.size!!) {
                    phases!![curPos].duration.toDouble()

                } else {
                    -1.0
                }

                val nameId : Int = if (curPos < phases?.size!!) {
                    phases!![curPos].phaseType.ordinal
                } else {
                    5
                }

                intent.putExtra(ACTION_ID_EXTRA, 2)
                intent.putExtra(TIME_EXTRA, time)
                intent.putExtra(NAME_ID_EXTRA, nameId)
            }
            else {
                intent.putExtra(ACTION_ID_EXTRA, 1)
                intent.putExtra(TIME_EXTRA, time)
                intent.putExtra(NAME_ID_EXTRA, phases!![curPos].phaseType.ordinal)
            }

            Gtime = time

            if(curPos < phases?.size!!)
                notifyBind()

            intent.putExtra(CURRENT_POSITION_EXTRA, curPos)
            intent.putExtra(MAX_POSITION_EXTRA, phases!!.size)
            intent.putExtra(OV_TIME_LEFT_EXTRA, countOvLeftTime())
            sendBroadcast(intent)
        }
    }

    private inner class SoundTask(private var tics: Int, private var rng: Ringtone) : TimerTask()
    {
        private var cur = 0

        override fun run() {

            if (cur == 0) {
                rng.play()
            }

            if (cur == tics) {
                rng.stop()
            }

            cur++
        }
    }

    private fun countOvLeftTime(): String {
        var secs :Int = Gtime.toInt()
        val l : Int = curPos + 1
        val r : Int = phases!!.size - 1
        for (i in l..r) {
            secs += phases!![i].duration
        }

        return String.format("%02d:%02d", secs / 60, secs % 60)
    }

    companion object {
        const val TIMER_UPDATED = "timerUpdate"
        const val TIME_EXTRA = "timeExtra"
        const val ID_EXTRA = "idExtra"
        const val ACTION_ID_EXTRA = "actionIdExtra"
        const val NAME_ID_EXTRA = "nameIdExtra"
        const val CURRENT_POSITION_EXTRA = "currentPositionIdExtra"
        const val MAX_POSITION_EXTRA = "maxPositionExtra"
        const val OV_TIME_LEFT_EXTRA = "overallTimeLeftExtra"
    }
}

