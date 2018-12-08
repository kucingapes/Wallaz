package tampan.kucingapes.simplewallpaper.ankoUi

import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.support.v4.nestedScrollView
import tampan.kucingapes.simplewallpaper.helper.BaseView.baseAppBar
import tampan.kucingapes.simplewallpaper.R

class AboutUi : AnkoComponent<AppCompatActivity> {
    override fun createView(ui: AnkoContext<AppCompatActivity>): View = with(ui) {
        coordinatorLayout {
            baseAppBar()

            nestedScrollView {

                verticalLayout {
                    imageView {
                        setImageResource(R.drawable.web_hi_res_512)
                    }.lparams(dip(120), dip(120)) {
                        gravity = Gravity.CENTER
                        margin = dip(52)
                    }

                    textView("Author") {

                    }


                }.lparams(matchParent, matchParent)


            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        }
    }
}