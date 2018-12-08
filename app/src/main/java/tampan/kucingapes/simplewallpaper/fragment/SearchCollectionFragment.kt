package tampan.kucingapes.simplewallpaper.fragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.androidnetworking.error.ANError
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.error
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.onRefresh
import tampan.kucingapes.simplewallpaper.helper.EndPoint
import tampan.kucingapes.simplewallpaper.helper.EndlessRecyclerView
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.adapter.CollectionAdapter
import tampan.kucingapes.simplewallpaper.i_view.ICollectionView
import tampan.kucingapes.simplewallpaper.model.collection.UnsplashCollection
import tampan.kucingapes.simplewallpaper.presenter.SearchCollectionPresenter

class SearchCollectionFragment : BaseFragment(), ICollectionView {

    private lateinit var searchCollectionPresenter: SearchCollectionPresenter
    private lateinit var collectionAdapter: CollectionAdapter

    private lateinit var searchView: SearchView
    private lateinit var querySearch: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchCollectionPresenter = SearchCollectionPresenter(this)
        searchCollectionPresenter.onView()
        searchView = SearchView(context)

        swipeRefresh.onRefresh {
            collectionList.clear()
            collectionAdapter.notifyDataSetChanged()
            searchCollectionPresenter.getData(EndPoint.SEARCH_COLLECTION, 1, querySearch)
        }
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
                searchCollectionPresenter.getData(EndPoint.SEARCH_COLLECTION, 1, query.toString())

                recyclerView.addOnScrollListener(object : EndlessRecyclerView(recyclerView.layoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        searchCollectionPresenter.getData(EndPoint.SEARCH_COLLECTION, page+1, query.toString())
                    }

                })
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                collectionList.clear()
                collectionAdapter.notifyDataSetChanged()
                return true
            }

        })

        searchView.setOnCloseListener {
            hideKeyboard()
            activity?.onBackPressed()
            true
        }
    }

    override fun onLoadData(list: MutableList<UnsplashCollection>) {
        collectionList.addAll(list)
        collectionAdapter.notifyDataSetChanged()
        swipeRefresh.isRefreshing = false
    }

    override fun onErrorData(anError: ANError) {
        error { "unsplash error -----> ${anError.message}" }
        swipeRefresh.isRefreshing = false
    }

    override fun initRecyclerView(view: View?) {
        recyclerView = view?.find(R.id.recycler_view) as RecyclerView
        collectionAdapter = CollectionAdapter(collectionList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = collectionAdapter

        val fabUp = activity?.find(R.id.fab_up) as FloatingActionButton
        fabUp.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
    }
}