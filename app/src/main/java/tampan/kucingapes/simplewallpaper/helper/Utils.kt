package tampan.kucingapes.simplewallpaper.helper

import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View

object Utils {
    fun ripple(view: View) {
        val typedValue = TypedValue()
        view.apply {
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                foreground = ContextCompat.getDrawable(context, typedValue.resourceId)
            }
        }
    }

    fun anGetStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}