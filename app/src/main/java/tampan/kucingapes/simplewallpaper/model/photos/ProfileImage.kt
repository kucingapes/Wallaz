package tampan.kucingapes.simplewallpaper.model.photos

import com.google.gson.annotations.SerializedName

data class ProfileImage(
    @SerializedName("large")
    val large: String?,
    @SerializedName("medium")
    val medium: String?,
    @SerializedName("small")
    val small: String?
)