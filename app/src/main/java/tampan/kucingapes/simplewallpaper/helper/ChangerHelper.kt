package tampan.kucingapes.simplewallpaper.helper

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import tampan.kucingapes.simplewallpaper.services.ChangerServices

object ChangerHelper {

    fun startServiceChanger(context: Context, interval: Long) {
        val timerManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val timerIntent = Intent(context, ChangerServices::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        val now = System.currentTimeMillis()
        timerManager.setRepeating(AlarmManager.RTC_WAKEUP, now, interval, timerIntent)
    }

    fun stopServiceChanger(context: Context) {
        val timerManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val timerIntent = Intent(context, ChangerServices::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        timerManager.cancel(timerIntent)
    }
}