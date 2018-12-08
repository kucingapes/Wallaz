package tampan.kucingapes.simplewallpaper.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import tampan.kucingapes.simplewallpaper.helper.ChangerHelper

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val sharedPreferences = context.getSharedPreferences("changer", Context.MODE_PRIVATE)
            val interval = sharedPreferences.getLong("interval", 0)
            if (interval != 0L) {
                ChangerHelper.startServiceChanger(context, interval)
            }
        }
    }
}