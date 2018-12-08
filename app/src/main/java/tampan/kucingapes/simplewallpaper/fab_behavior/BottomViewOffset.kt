package tampan.kucingapes.simplewallpaper.fab_behavior

import android.support.design.widget.AppBarLayout
import android.view.View

class BottomViewOffset(private val parent: View, private val view: View) : AppBarLayout.OnOffsetChangedListener {
    private var bottomTranslationX = 0.0f

    override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
        val displacementFraction = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
        val topUntranslated = view.top + view.translationY - bottomTranslationX
        val fullDisplacement = parent.bottom - topUntranslated
        val bottomTranslationY = fullDisplacement * displacementFraction

        view.translationY = bottomTranslationY - bottomTranslationX + view.translationY
        bottomTranslationX = bottomTranslationY
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val value = other as BottomViewOffset?

        return parent == value?.parent && view == value.view

    }

    override fun hashCode(): Int {
        var result = parent.hashCode()
        result = 31 * result + view.hashCode()
        return result
    }
}