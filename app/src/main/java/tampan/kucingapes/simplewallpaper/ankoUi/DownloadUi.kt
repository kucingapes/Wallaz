package tampan.kucingapes.simplewallpaper.ankoUi

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.helper.Utils

class DownloadUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui){
        linearLayout {
            lparams(matchParent, dip(50))
            gravity = Gravity.CENTER_VERTICAL
            Utils.ripple(this)
            imageView {
                id = R.id.download_badge
            }.lparams(wrapContent, wrapContent) {
                leftMargin = dip(8)
                rightMargin = dip(8)
            }

            textView {
                id = R.id.download_text
            }.lparams(wrapContent, wrapContent)
        }
    }
}