package tampan.kucingapes.simplewallpaper.ankoUi

import android.support.v7.app.AppCompatActivity
import android.view.View
import org.jetbrains.anko.*
import tampan.kucingapes.simplewallpaper.helper.BaseView.baseAppBar
import tampan.kucingapes.simplewallpaper.R

class PreferencesUi : AnkoComponent<AppCompatActivity> {
    override fun createView(ui: AnkoContext<AppCompatActivity>): View = with(ui){
        verticalLayout {
            baseAppBar()

            verticalLayout {
                padding = dip(12)

                textView("Layout type") {
                }.lparams {
                    margin = dip(6)
                }

                spinner {
                    id = R.id.layout_type
                }.lparams(matchParent, wrapContent) {
                    margin = dip(6)
                }
            }.lparams(matchParent, matchParent)
        }
    }
}