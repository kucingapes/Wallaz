package tampan.kucingapes.simplewallpaper.ankoUi

import android.content.Context
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7._RecyclerView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import tampan.kucingapes.simplewallpaper.helper.LayoutType
import tampan.kucingapes.simplewallpaper.R

class MainFragmentUi<T> : AnkoComponent<T> {
    override fun createView(ui: AnkoContext<T>): View = with(ui) {
        swipeRefreshLayout {
            id = R.id.swipe_refresh
            val sharedPref = context?.getSharedPreferences("layout", Context.MODE_PRIVATE)
            val layoutType = sharedPref?.getString("type", LayoutType.GRID)

            recyclerView {
                when(layoutType) {
                    LayoutType.GRID_CARD -> {
                        setGridUi()
                    }
                    LayoutType.VERTICAL_CARD -> {
                        setGridUi()
                    }
                }

                id = R.id.recycler_view
            }
        }
    }

    private fun @AnkoViewDslMarker _RecyclerView.setGridUi() {
        padding = dip(6)
        clipToPadding = false
    }
}