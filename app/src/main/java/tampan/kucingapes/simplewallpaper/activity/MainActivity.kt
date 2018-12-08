package tampan.kucingapes.simplewallpaper.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.kucingapes.ankodrawer.*
import com.kucingapes.ankodrawer.AnDrawerView.anDrawerLayoutWithToolbar
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.navigationIconResource
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.ankoUi.HeaderDrawerUi
import tampan.kucingapes.simplewallpaper.ankoUi.MainUi
import tampan.kucingapes.simplewallpaper.fab_behavior.BottomViewOffset
import tampan.kucingapes.simplewallpaper.fragment.CollectionFragment
import tampan.kucingapes.simplewallpaper.fragment.MainFragment
import tampan.kucingapes.simplewallpaper.fragment.SearchCollectionFragment
import tampan.kucingapes.simplewallpaper.fragment.SearchPhotoFragment
import java.io.File

class MainActivity : BaseActivity(), AnDrawerClickListener {

    private lateinit var drawer: AnDrawer
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var parentLayout: CoordinatorLayout
    private lateinit var fabUp: FloatingActionButton
    private lateinit var toolbar: Toolbar

    private lateinit var mainFragment: MainFragment
    private lateinit var collectionFragment: CollectionFragment
    private lateinit var mainFragmentWithOrder: MainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drawer = AnDrawer(this, R.color.colorPrimaryDark)
        frameLayout { anDrawerLayoutWithToolbar(drawer) }
        AnDrawerInit.setupMainView(this, MainUi())
        AnDrawerInit.setupHeader(this, HeaderDrawerUi())
        drawer.setNavigationStyle(AnDrawerView.STYLE.NEW_MATERIAL)

        bindId()
        addItemDrawer()

        mainFragment = MainFragment()
        collectionFragment = CollectionFragment()
        mainFragmentWithOrder = MainFragment.newConfig("", "popular")

        initToolbarFab()

        initPermission()

        replaceFragment(mainFragment)
    }

    private fun initToolbarFab() {
        toolbar.navigationIconResource = R.drawable.ic_custom_nav
        setSupportActionBar(toolbar)

        val syncAppBar = BottomViewOffset(parentLayout, fabUp)
        appBarLayout.addOnOffsetChangedListener(syncAppBar)
    }

    private fun initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                            setupFolder()
                        }

                        override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                            token?.continuePermissionRequest()
                        }

                    }).check()
        } else {
            setupFolder()
        }
    }

    private fun setupFolder() {
        val path = File(Environment.getExternalStorageDirectory(), getString(R.string.path_changer))
        if (!path.exists()) {
            path.mkdirs()
        }
    }

    private fun addItemDrawer() {
        val homeItem = AnDrawerItem("Home")
                .addIcon(R.drawable.ic_photo)
                .addIdentifier(1)

        val collectionItem = AnDrawerItem("Collection")
                .addIcon(R.drawable.ic_collection)
                .addIdentifier(2)

        val popularItem = AnDrawerItem("Popular")
                .addIcon(R.drawable.ic_popular)
                .addIdentifier(3)

        val changerItem = AnDrawerItem("Auto Wallpaper")
                .addIcon(R.drawable.ic_live_wallpaper)
                .addIdentifier(4).setFocusable(false)

        val preferencesItem = AnDrawerItem("Preferences")
                .addIcon(R.drawable.ic_setting)
                .addIdentifier(5).setFocusable(false)
        val divider = AnDrawerItem(AnDrawerItem.DIVIDER)

        drawer.addItems().apply {
            add(divider)
            add(homeItem)
            add(collectionItem)
            add(popularItem)
            add(divider)
            add(changerItem)
            add(preferencesItem)
        }

        drawer.setSelectedItem(1)
    }

    override fun onDrawerClick(identifier: Int) {
        super.onDrawerClick(identifier)
        when (identifier) {
            1 -> replaceFragment(mainFragment)
            2 -> replaceFragment(collectionFragment)
            3 -> replaceFragment(mainFragmentWithOrder)
            4 -> startActivity<ChangerActivity>()
            5 -> {
                startActivity<PreferencesActivity>()
                finish()
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuSearch = menu?.findItem(R.id.action_search)
        menuSearch?.setOnMenuItemClickListener {
            if (mainFragment.isVisible || mainFragmentWithOrder.isVisible) {
                replaceFragment(SearchPhotoFragment(), "search_photo")
            }
            if (collectionFragment.isVisible) {
                replaceFragment(SearchCollectionFragment(), "search_collection")
            }
            true
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                AnDrawerInit.openDrawer(this)
                return true
            }
        }
        return false
    }

    private fun bindId() {
        appBarLayout = find(R.id.app_bar)
        parentLayout = find(R.id.parent)
        fabUp = find(R.id.fab_up)
        toolbar = find(R.id.toolbar)
    }
}
