package tampan.kucingapes.simplewallpaper.model.collection

import com.google.gson.annotations.SerializedName

data class ResultCollection(
        @SerializedName("results")
        val results: MutableList<UnsplashCollection>,
        @SerializedName("total")
        val total: Int?,
        @SerializedName("total_pages")
        val totalPages: Int?
)