package ir.sharif

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {
    @GET("/users/{username}")
    fun getUser(@Path("username") username: String): Call<GithubUser>
    @GET("/users/{username}/repos")
    fun getUserRepos(@Path("username") username: String): Call<List<GithubRepo>>
}