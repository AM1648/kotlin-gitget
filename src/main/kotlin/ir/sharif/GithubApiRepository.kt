package ir.sharif

import retrofit2.Retrofit

class GithubApiRepository(retrofit: Retrofit) {
    private val apiService = retrofit.create(GithubApi::class.java)

    private val userCache = HashMap<String, GithubUser>()
    private val repoCache = HashMap<String, MutableSet<String>>()

    fun getUserByName(username: String): GithubUser? {
        return userCache[username] ?: fetchUser(username)?.also { user ->
            userCache[username] = user
            for (repo in user.repos) {
                if (!repoCache.containsKey(repo)) {
                    repoCache[repo] = mutableSetOf()
                }
                repoCache[repo]!!.add(username)
            }
        }
    }

    fun getCachedUserByName(username: String): GithubUser? {
        return userCache[username]
    }

    fun getAllCachedUsernames(): Set<String> {
        return userCache.keys
    }

    fun getUsernamesByRepoName(repoName: String): Set<String> {
        return repoCache[repoName] ?: mutableSetOf()
    }

    private fun fetchUser(username: String): GithubUser? {
        try {
            val response = apiService.getUser(username).execute()
            if (response.isSuccessful) {
                val user = response.body()!!
                user.repos = apiService.getUserRepos(username).execute().body()!!.map { it.name }
                return user
            } else {
                println("Cannot fetch user '$username': ${response.code()} ${response.message()}")
                return null
            }
        } catch (e: Exception) {
            println("Error fetching user '$username': ${e.message}")
            return null
        }
    }
}