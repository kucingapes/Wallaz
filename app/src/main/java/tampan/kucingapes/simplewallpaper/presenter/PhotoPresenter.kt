package tampan.kucingapes.simplewallpaper.presenter

import android.support.v4.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import tampan.kucingapes.simplewallpaper.BuildConfig
import tampan.kucingapes.simplewallpaper.i_view.IPhotoView
import tampan.kucingapes.simplewallpaper.model.photos.Unsplash

class PhotoPresenter(private val context: Fragment?) {

    private val view = context as IPhotoView

    fun getData(endPoint: String, page: Int, order: String) {
        AndroidNetworking.get(BuildConfig.BASEURL)
                .addPathParameter("endpoint", endPoint)
                .addQueryParameter("client_id", BuildConfig.APIKEY)
                .addQueryParameter("per_page", "30")
                .addQueryParameter("page", "$page")
                .addQueryParameter("order_by", order)
                .build()
                .getAsObjectList(Unsplash::class.java, object : ParsedRequestListener<MutableList<Unsplash>> {
                    override fun onResponse(response: MutableList<Unsplash>) {
                        view.onLoadData(response)
                    }

                    override fun onError(anError: ANError) {
                        view.onErrorData(anError)
                    }

                })
    }

    fun onView() {
        view.initRecyclerView(context?.view)
    }
}