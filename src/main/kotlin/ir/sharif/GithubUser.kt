package ir.sharif

import com.google.gson.annotations.SerializedName

data class GithubUser(
    @SerializedName("name")
    val name: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("followers")
    val followers: String,
    @SerializedName("following")
    val following: String,
    var repos: List<String>
)