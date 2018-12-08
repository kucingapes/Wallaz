package tampan.kucingapes.simplewallpaper.ankoUi

import android.support.v7.app.AppCompatActivity
import android.view.View
import org.jetbrains.anko.*
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.helper.Utils

class InCollectionUi : AnkoComponent<AppCompatActivity> {
    override fun createView(ui: AnkoContext<AppCompatActivity>): View = with(ui) {
        verticalLayout {
            view {
                backgroundColorResource = R.color.colorPrimaryDark
            }.lparams(matchParent, Utils.anGetStatusBarHeight(context))

            frameLayout {
                id = R.id.main_ui
            }.lparams(matchParent, matchParent)
        }
    }
}