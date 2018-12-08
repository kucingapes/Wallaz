package tampan.kucingapes.simplewallpaper.ankoUi

import android.annotation.SuppressLint
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.nestedScrollView
import tampan.kucingapes.simplewallpaper.helper.BaseView.baseAppBar
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.helper.Utils

class ChangerUi : AnkoComponent<AppCompatActivity> {
    @SuppressLint("SetTextI18n")
    override fun createView(ui: AnkoContext<AppCompatActivity>): View = with(ui) {
        verticalLayout {
            view {
                backgroundColorResource = R.color.colorPrimaryDark
            }.lparams(matchParent, Utils.anGetStatusBarHeight(context))

            coordinatorLayout {
                baseAppBar()
                nestedScrollView {
                    verticalLayout {
                        padding = dip(12)

                        textView {
                            id = R.id.status
                        }

                        textView {
                            id = R.id.sum_wallpaper
                        }

                        spinner {
                            id = R.id.spinner_interval
                        }

                        switch {
                            text = "Activate wallpaper changer"
                            id = R.id.switcher
                        }

                        recyclerView {
                            id = R.id.recycler_view
                            clipToPadding = false
                            isNestedScrollingEnabled = false
                        }.lparams(matchParent, wrapContent) {
                            topMargin = dip(20)
                        }

                    }.lparams(matchParent, matchParent)

                }.lparams(matchParent, matchParent) {
                    behavior = AppBarLayout.ScrollingViewBehavior()
                }
            }
        }
    }
}