package tampan.kucingapes.simplewallpaper.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*
import com.androidnetworking.error.ANError
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
import tampan.kucingapes.simplewallpaper.presenter.PhotoPresenter

class MainFragment : BaseFragment(), IPhotoView {

    private lateinit var photoPresenter: PhotoPresenter
    private lateinit var mainAdapter: MainAdapter

    companion object {
        fun newConfig(endPoint: String, order: String): MainFragment {
            val mainFragment = MainFragment()
            val bundle = Bundle()
            bundle.putString("endPoint", endPoint)
            bundle.putString("order", order)
            mainFragment.arguments = bundle
            return mainFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            endPointType = it.getString("endPoint", "")
            order = it.getString("order", "latest")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        photoPresenter = PhotoPresenter(this)
        photoPresenter.getData("$endPointType${EndPoint.PHOTOS}", 1, order)
        photoPresenter.onView()

        swipeRefresh.isRefreshing = true
        swipeRefresh.onRefresh {
            mainList.clear()
            mainAdapter.notifyDataSetChanged()
            photoPresenter.getData("$endPointType${EndPoint.PHOTOS}", 1, order)
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

        recyclerView.addOnScrollListener(object : EndlessRecyclerView(recyclerView.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                photoPresenter.getData("$endPointType${EndPoint.PHOTOS}", page + 1, order)
            }
        })

        val fabUp = activity?.find(R.id.fab_up) as FloatingActionButton
        fabUp.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
        }
    }

}