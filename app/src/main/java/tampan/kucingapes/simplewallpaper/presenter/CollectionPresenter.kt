package tampan.kucingapes.simplewallpaper.presenter

import android.support.v4.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import org.jetbrains.anko.support.v4.toast
import tampan.kucingapes.simplewallpaper.BuildConfig
import tampan.kucingapes.simplewallpaper.i_view.ICollectionView
import tampan.kucingapes.simplewallpaper.model.collection.UnsplashCollection

class CollectionPresenter(private val context: Fragment) {
    private val view = context as ICollectionView

    fun getData(endPoint: String, page: Int) {
        AndroidNetworking.get(BuildConfig.BASEURL)
                .addPathParameter("endpoint", endPoint)
                .addQueryParameter("client_id", BuildConfig.APIKEY)
                .addQueryParameter("per_page", "30")
                .addQueryParameter("page", "$page")
                .build()
                .getAsObjectList(UnsplashCollection::class.java, object : ParsedRequestListener<MutableList<UnsplashCollection>> {
                    override fun onResponse(response: MutableList<UnsplashCollection>) {
                        view.onLoadData(response)
                    }

                    override fun onError(anError: ANError) {
                        view.onErrorData(anError)
                    }

                })
    }

    fun onView() {
        view.initRecyclerView(context.view)
    }
}