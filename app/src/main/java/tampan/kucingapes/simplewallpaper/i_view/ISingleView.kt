package tampan.kucingapes.simplewallpaper.i_view

import com.androidnetworking.error.ANError
import tampan.kucingapes.simplewallpaper.model.photos.Unsplash

interface ISingleView {
    fun onLoadSingle(unsplash: Unsplash)
    fun onError(anError: ANError)
}