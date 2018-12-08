package tampan.kucingapes.simplewallpaper.ankoUi

import android.os.Build
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import tampan.kucingapes.simplewallpaper.helper.BaseView.baseAppBarWithStatusBar
import tampan.kucingapes.simplewallpaper.helper.BaseView.photoView
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.helper.Utils

class ImgUi : AnkoComponent<AppCompatActivity> {
    override fun createView(ui: AnkoContext<AppCompatActivity>): View = with(ui) {
        coordinatorLayout {
            id = R.id.parent
            baseAppBarWithStatusBar()

            relativeLayout {

                progressBar {
                    isIndeterminate = true
                    id = R.id.progress_circular
                }.lparams(dip(300), wrapContent) {
                    centerInParent()
                }

                textView {
                    id = R.id.error_text
                    visibility = View.GONE
                }.lparams {
                    centerInParent()
                }

                photoView {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        transitionName = "image"
                    }
                    id = R.id.img_view
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }.lparams(matchParent, matchParent)


                linearLayout {
                    id = R.id.bottom_ui
                    gravity = Gravity.CENTER
                    floatingActionButton {
                        id = R.id.fab_hq
                        size = FloatingActionButton.SIZE_MINI
                        imageResource = R.drawable.ic_hq
                    }

                    floatingActionButton {
                        id = R.id.fab_menu
                        imageResource = R.drawable.ic_menu_up
                    }.lparams {
                        margin = dip(12)
                    }
                }.lparams(wrapContent, wrapContent) {
                    alignParentBottom()
                    alignParentRight()
                }

                view {
                    backgroundColorResource = R.color.tintBg
                }.lparams(matchParent, Utils.anGetStatusBarHeight(context))

            }.lparams(matchParent, matchParent)

        }
    }


}