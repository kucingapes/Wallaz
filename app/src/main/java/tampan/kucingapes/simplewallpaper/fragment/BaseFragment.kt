package tampan.kucingapes.simplewallpaper.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.adapter.MainAdapter
import tampan.kucingapes.simplewallpaper.ankoUi.MainFragmentUi
import tampan.kucingapes.simplewallpaper.model.collection.UnsplashCollection
import tampan.kucingapes.simplewallpaper.model.photos.Unsplash

open class BaseFragment : Fragment(), AnkoLogger {

    var endPointType = ""
    var order = ""

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefresh: SwipeRefreshLayout

    var mainList: MutableList<Unsplash> = mutableListOf()
    var collectionList: MutableList<UnsplashCollection> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val ui = context?.let {
            AnkoContext.create(it, this)
        }?.let {
            MainFragmentUi<Fragment>().createView(it)
        }

        swipeRefresh = ui?.find(R.id.swipe_refresh) as SwipeRefreshLayout

        return ui
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}