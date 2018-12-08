package tampan.kucingapes.simplewallpaper.model.collection

import com.google.gson.annotations.SerializedName

data class PreviewPhoto(
        @SerializedName("id")
        val id: String?,
        @SerializedName("urls")
        val urls: Urls?
)