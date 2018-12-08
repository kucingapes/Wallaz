package tampan.kucingapes.simplewallpaper.model.collection

import com.google.gson.annotations.SerializedName

data class Links(
        @SerializedName("html")
        val html: String?,
        @SerializedName("photos")
        val photos: String?,
        @SerializedName("related")
        val related: String?,
        @SerializedName("self")
        val self: String?
)