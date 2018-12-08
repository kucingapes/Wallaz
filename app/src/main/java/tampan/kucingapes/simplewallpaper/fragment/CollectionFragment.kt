package tampan.kucingapes.simplewallpaper.fragment

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.androidnetworking.error.ANError
import org.jetbrains.anko.error
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.onRefresh
import tampan.kucingapes.simplewallpaper.helper.EndPoint
import tampan.kucingapes.simplewallpaper.helper.EndlessRecyclerView
import tampan.kucingapes.simplewallpaper.R
import tampan.kucingapes.simplewallpaper.adapter.CollectionAdapter
import tampan.kucingapes.simplewallpaper.i_view.ICollectionView
import tampan.kucingapes.simplewallpaper.model.collection.UnsplashCollection
import tampan.kucingapes.simplewallpaper.presenter.CollectionPresenter

class CollectionFragment : BaseFragment(), ICollectionView {

    private lateinit var collectionPresenter: CollectionPresenter
    private lateinit var collectionAdapter: CollectionAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        collectionPresenter = CollectionPresenter(this)
        collectionPresenter.getData(EndPoint.COLLECTION, 1)
        collectionPresenter.onView()

        swipeRefresh.isRefreshing = true
        swipeRefresh.onRefresh {
            collectionList.clear()
            collectionAdapter.notifyDataSetChanged()
            collectionPresenter.getData(EndPoint.COLLECTION, 1)
        }
    }

    override fun onLoadData(list: MutableList<UnsplashCollection>) {
        collectionList.addAll(list)
        collectionAdapter.notifyDataSetChanged()

        swipeRefresh.isRefreshing = false
    }

    override fun onErrorData(anError: ANError) {
        error { "collection error -----> ${anError.message}" }
    }

    override fun initRecyclerView(view: View?) {
        collectionAdapter = CollectionAdapter(collectionList)
        recyclerView = view?.find(R.id.recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = collectionAdapter

        recyclerView.addOnScrollListener(object : EndlessRecyclerView(recyclerView.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                collectionPresenter.getData(EndPoint.COLLECTION, page+1)
            }
        })

        val fabUp = activity?.find(R.id.fab_up) as FloatingActionButton
        fabUp.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
    }
}