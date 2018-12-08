package tampan.kucingapes.simplewallpaper.services

import android.app.WallpaperManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Environment
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import tampan.kucingapes.simplewallpaper.R
import java.io.File
import java.util.*

class ChangerServices : BroadcastReceiver(), AnkoLogger {
    override fun onReceive(context: Context?, intent: Intent?) {
        val path = File(Environment.getExternalStorageDirectory(), context?.getString(R.string.path_changer))
        val files = path.listFiles()
        val random = Random()

        val file = files[random.nextInt(files.size)]
        val wallpaper = BitmapFactory.decodeFile(file.absolutePath)

        val wallpaperManager = WallpaperManager.getInstance(context)
        wallpaperManager.setBitmap(wallpaper)

        info { "wallpaper changing now !!" }
    }
}