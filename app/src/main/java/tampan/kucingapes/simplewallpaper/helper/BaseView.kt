package tampan.kucingapes.simplewallpaper.helper

import android.os.Build
import android.support.design.widget.AppBarLayout
import android.view.ViewManager
import android.widget.TextView
import com.github.chrisbanes.photoview.PhotoView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.themedToolbar
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.design.appBarLayout
import tampan.kucingapes.simplewallpaper.R

object BaseView {

    fun ViewManager.baseAppBar() = appBarLayout {
        lparams(matchParent, wrapContent)
        id = R.id.app_bar
        themedToolbar(R.style.ThemeOverlay_AppCompat_Dark) {
            backgroundColorResource = R.color.colorPrimary
            id = R.id.toolbar
            title = context.getString(R.string.app_name)
            setTitleTextColor(resources.getColor(R.color.colorAccent))

            val titleView = this.getChildAt(0) as TextView
            titleView.id = R.id.title
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                titleView.transitionName = "title"
            }

        }.lparams(matchParent, dimenAttr(R.attr.actionBarSize)) {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }
    }

    fun ViewManager.baseAppBarWithStatusBar() = appBarLayout {
        lparams(matchParent, wrapContent)
        id = R.id.app_bar

        verticalLayout {
            view {
                backgroundColorResource = R.color.colorAccent
            }.lparams(matchParent, Utils.anGetStatusBarHeight(context))

            themedToolbar {
                backgroundColorResource = R.color.colorPrimary
                id = R.id.toolbar
                title = context.getString(R.string.app_name)
                setTitleTextColor(resources.getColor(R.color.colorAccent))

                val titleView = this.getChildAt(0) as TextView
                titleView.id = R.id.title
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    titleView.transitionName = "title"
                }
            }.lparams(matchParent, dimenAttr(R.attr.actionBarSize))
        }.lparams(matchParent, wrapContent) {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }
    }


    inline fun ViewManager.photoView(init: PhotoView.() -> Unit): PhotoView {
        return ankoView({ PhotoView(it) }, theme = 0, init = init)
    }

}