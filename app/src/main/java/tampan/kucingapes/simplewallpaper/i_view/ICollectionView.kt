package tampan.kucingapes.simplewallpaper.i_view

import android.view.View
import com.androidnetworking.error.ANError
import tampan.kucingapes.simplewallpaper.model.collection.UnsplashCollection

interface ICollectionView {
    fun onLoadData(list: MutableList<UnsplashCollection>)
    fun onErrorData(anError: ANError)
    fun initRecyclerView(view: View?)
}