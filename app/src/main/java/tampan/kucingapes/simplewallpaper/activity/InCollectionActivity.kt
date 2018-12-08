package tampan.kucingapes.simplewallpaper.activity

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.ViewGroup
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.ankoUi.InCollectionUi
import tampan.kucingapes.simplewallpaper.ankoUi.MainUi
import tampan.kucingapes.simplewallpaper.fab_behavior.BottomViewOffset
import tampan.kucingapes.simplewallpaper.fragment.MainFragment

class InCollectionActivity : BaseActivity() {

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var parentLayout: CoordinatorLayout
    private lateinit var fabUp: FloatingActionButton
    private lateinit var toolbar: Toolbar

    private lateinit var endPoint: String
    private lateinit var title: String

    private lateinit var mainFragment: MainFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        InCollectionUi().setContentView(this)
        val root = find(R.id.main_ui) as ViewGroup
        root.addView(MainUi().createView(AnkoContext.create(root.context, root)))
        supportPostponeEnterTransition()

        bindId()
        endPoint = intent.getStringExtra("id_collection")
        title = intent.getStringExtra("title")

        toolbar.navigationIconResource = R.drawable.ic_back
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
        supportStartPostponedEnterTransition()

        mainFragment = MainFragment.newConfig(endPoint, "latest")

        val syncAppBar = BottomViewOffset(parentLayout, fabUp)
        appBarLayout.addOnOffsetChangedListener(syncAppBar)

        replaceFragment(mainFragment)

    }

    private fun bindId() {
        appBarLayout = find(R.id.app_bar)
        parentLayout = find(R.id.parent)
        fabUp = find(R.id.fab_up)
        toolbar = find(R.id.toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
        }
        return false
    }
}