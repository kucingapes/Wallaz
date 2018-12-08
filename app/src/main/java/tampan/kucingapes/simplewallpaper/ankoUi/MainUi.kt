package tampan.kucingapes.simplewallpaper.ankoUi

import android.support.design.widget.AppBarLayout
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import tampan.kucingapes.simplewallpaper.helper.BaseView.baseAppBar
import tampan.kucingapes.simplewallpaper.R

class MainUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        coordinatorLayout {
            id = R.id.parent
            baseAppBar()

            frameLayout {
                id = R.id.container
            }.lparams(matchParent, matchParent) {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }

            floatingActionButton {
                id = R.id.fab_up
                imageResource = R.drawable.ic_up
            }.lparams {
                gravity = Gravity.END or Gravity.BOTTOM
                margin = dip(12)
            }

        }
    }

}