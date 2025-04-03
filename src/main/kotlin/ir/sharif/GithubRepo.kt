package ir.sharif

import com.google.gson.annotations.SerializedName

data class GithubRepo(
    @SerializedName("name")
    val name: String
)
