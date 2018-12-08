package tampan.kucingapes.simplewallpaper.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.androidnetworking.AndroidNetworking
import org.jetbrains.anko.AnkoLogger
import tampan.kucingapes.simplewallpaper.R

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidNetworking.initialize(this)

        window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        registerChannelNotification()
    }

    private fun registerChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Download"
            val descriptionText = "Notification when download image"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("id", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    fun AppCompatActivity.replaceFragment(fragment: Fragment, backStackTag: String? = null) {
        supportFragmentManager.inTransaction {
            replace(R.id.container, fragment)
            backStackTag?.let { addToBackStack(fragment.javaClass.name) }
        }
    }

}