package tampan.kucingapes.simplewallpaper.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.*
import android.view.*
import com.androidnetworking.error.ANError
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.error
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.onRefresh
import tampan.kucingapes.simplewallpaper.helper.EndPoint
import tampan.kucingapes.simplewallpaper.helper.EndlessRecyclerView
import tampan.kucingapes.simplewallpaper.helper.LayoutType
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.adapter.MainAdapter
import tampan.kucingapes.simplewallpaper.i_view.IPhotoView
import tampan.kucingapes.simplewallpaper.model.photos.Unsplash
import tampan.kucingapes.simplewallpaper.presenter.SearchPhotoPresenter

class SearchPhotoFragment : BaseFragment(), IPhotoView {

    private lateinit var searchPhotoPresenter: SearchPhotoPresenter
    private lateinit var mainAdapter: MainAdapter

    private lateinit var searchView: SearchView

    private lateinit var querySearch: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchPhotoPresenter = SearchPhotoPresenter(this)
        searchPhotoPresenter.onView()
        searchView = SearchView(context)

        swipeRefresh.onRefresh {
            mainList.clear()
            mainAdapter.notifyDataSetChanged()
            searchPhotoPresenter.getData(EndPoint.SEARCH_PHOTOS, 1, querySearch)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchMenu = menu?.findItem(R.id.action_search)
        searchMenu?.actionView = searchView
        val searchViewPlate = searchView.find(android.support.v7.appcompat.R.id.search_plate) as View
        searchViewPlate.backgroundColorResource = R.color.colorPrimary

        searchView.isIconified = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()
                swipeRefresh.isRefreshing = true
                querySearch = query.toString()
                searchPhotoPresenter.getData(EndPoint.SEARCH_PHOTOS, 1, query.toString())

                recyclerView.addOnScrollListener(object : EndlessRecyclerView(recyclerView.layoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        searchPhotoPresenter.getData(EndPoint.SEARCH_PHOTOS, page + 1, query.toString())
                    }

                })
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainList.clear()
                mainAdapter.notifyDataSetChanged()
                return true
            }

        })

        searchView.setOnCloseListener {
            hideKeyboard()
            activity?.onBackPressed()
            true
        }
    }

    override fun onLoadData(list: MutableList<Unsplash>) {
        mainList.addAll(list)
        mainAdapter.notifyDataSetChanged()
        swipeRefresh.isRefreshing = false
    }

    override fun onErrorData(anError: ANError) {
        error { "unsplash error -----> ${anError.message}" }
        swipeRefresh.isRefreshing = false
    }

    override fun initRecyclerView(view: View?) {
        val sharedPref = context?.getSharedPreferences("layout", Context.MODE_PRIVATE)
        val layoutType = sharedPref?.getString("type", LayoutType.GRID)

        mainAdapter = MainAdapter(mainList)
        recyclerView = view?.find(R.id.recycler_view) as RecyclerView

        if (layoutType == LayoutType.GRID || layoutType == LayoutType.GRID_CARD) {
            recyclerView.layoutManager = GridLayoutManager(context, 2)
        }
        if (layoutType == LayoutType.VERTICAL || layoutType == LayoutType.VERTICAL_CARD) {
            recyclerView.layoutManager = StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL)
        }

        recyclerView.adapter = mainAdapter


        val fabUp = activity?.find(R.id.fab_up) as FloatingActionButton
        fabUp.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
    }

}