package tampan.kucingapes.simplewallpaper.model.photos

import com.google.gson.annotations.SerializedName

data class Result(
        @SerializedName("results")
        val results: MutableList<Unsplash>,
        @SerializedName("total")
        val total: Int?,
        @SerializedName("total_pages")
        val totalPages: Int?
)