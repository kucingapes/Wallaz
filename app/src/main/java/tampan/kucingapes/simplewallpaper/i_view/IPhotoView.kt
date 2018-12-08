package tampan.kucingapes.simplewallpaper.i_view

import android.view.View
import com.androidnetworking.error.ANError
import tampan.kucingapes.simplewallpaper.model.photos.Unsplash

interface IPhotoView {
    fun onLoadData(list: MutableList<Unsplash>)
    fun onErrorData(anError: ANError)
    fun initRecyclerView(view: View?)
}