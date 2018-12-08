package tampan.kucingapes.simplewallpaper.ankoUi

import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.*
import tampan.kucingapes.simplewallpaper.R

class HeaderDrawerUi : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        verticalLayout {
            lparams(matchParent, dip(200))

            backgroundColorResource = R.color.colorPrimaryDark
        }
    }
}