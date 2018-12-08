package tampan.kucingapes.simplewallpaper.presenter

import android.content.Context
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import tampan.kucingapes.simplewallpaper.BuildConfig
import tampan.kucingapes.simplewallpaper.i_view.ISingleView
import tampan.kucingapes.simplewallpaper.model.photos.Unsplash

class SinglePresenter(context: Context) {
    private val view = context as ISingleView

    fun getData(id: String) {
        AndroidNetworking.get(BuildConfig.BASEURL)
            .addPathParameter("endpoint", "photos/$id")
            .addQueryParameter("client_id", BuildConfig.APIKEY)
            .build()
            .getAsObject(Unsplash::class.java, object : ParsedRequestListener<Unsplash> {
                override fun onResponse(response: Unsplash) {
                    view.onLoadSingle(response)
                }

                override fun onError(anError: ANError) {
                    view.onError(anError)
                }

            })

    }
}