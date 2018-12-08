package tampan.kucingapes.simplewallpaper.presenter

import android.support.v4.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import tampan.kucingapes.simplewallpaper.BuildConfig
import tampan.kucingapes.simplewallpaper.i_view.ICollectionView
import tampan.kucingapes.simplewallpaper.model.collection.ResultCollection

class SearchCollectionPresenter(private val context: Fragment) {
    private val view = context as ICollectionView

    fun getData(endPoint: String, page: Int, query: String) {
        AndroidNetworking.get(BuildConfig.BASEURL)
                .addPathParameter("endpoint", endPoint)
                .addQueryParameter("client_id", BuildConfig.APIKEY)
                .addQueryParameter("per_page", "30")
                .addQueryParameter("page", "$page")
                .addQueryParameter("query", query)
                .build()
                .getAsObject(ResultCollection::class.java, object : ParsedRequestListener<ResultCollection> {
                    override fun onResponse(response: ResultCollection) {
                        view.onLoadData(response.results)
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