package tampan.kucingapes.simplewallpaper.model.photos

import com.google.gson.annotations.SerializedName

data class Unsplash(
        @SerializedName("categories")
    val categories: List<Any?>?,
        @SerializedName("color")
    val color: String?,
        @SerializedName("created_at")
    val createdAt: String?,
        @SerializedName("current_user_collections")
    val currentUserCollections: List<Any?>?,
        @SerializedName("description")
    val description: Any?,
        @SerializedName("height")
    val height: Int?,
        @SerializedName("id")
    val id: String?,
        @SerializedName("liked_by_user")
    val likedByUser: Boolean?,
        @SerializedName("likes")
    val likes: Int?,
        @SerializedName("links")
    val links: Links?,
        @SerializedName("slug")
    val slug: Any?,
        @SerializedName("sponsored")
    val sponsored: Boolean?,
        @SerializedName("sponsored_by")
    val sponsoredBy: Any?,
        @SerializedName("sponsored_impressions_id")
    val sponsoredImpressionsId: Any?,
        @SerializedName("updated_at")
    val updatedAt: String?,
        @SerializedName("urls")
    val urls: Urls?,
        @SerializedName("user")
    val user: User?,
        @SerializedName("width")
    val width: Int?
)