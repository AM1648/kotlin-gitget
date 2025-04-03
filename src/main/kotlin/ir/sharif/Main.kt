package ir.sharif

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.format.DateTimeFormatter

fun main() {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val githubApiRepository = GithubApiRepository(retrofit)

    println("Actions:")
    println("1) Get user info")
    println("2) See list of fetched users")
    println("3) Search in fetched users")
    println("4) Search in fetched repository names")
    println("5) Exit")

    while (true) {
        print("Action number: ")
        val action = readlnOrNull()?.toIntOrNull()
        when (action) {
            1 -> {
                print("Username: ")
                val username = readln().lowercase()
                githubApiRepository.getUserByName(username)?.let(::printUserInfo)
            } 2 -> {
                val usernames = githubApiRepository.getAllCachedUsernames()
                if (usernames.isEmpty()) {
                    println("No user cached yet.")
                } else {
                    usernames.forEach { println("- $it") }
                }
            } 3 -> {
                print("Username: ")
                val username = readln().lowercase()
                val user = githubApiRepository.getCachedUserByName(username)
                if (user == null) {
                    println("No such user in cache.")
                } else {
                    printUserInfo(user)
                }
            } 4 -> {
                print("Repo name: ")
                val repoName = readln().lowercase()
                val usernames = githubApiRepository.getUsernamesByRepoName(repoName)
                if (usernames.isEmpty()) {
                    println("No user cached with such repo.")
                } else {
                    usernames.forEach { println("- $it") }
                }
            } 5 -> {
                return
            } else -> {
                println("Invalid action.")
            }
        }
    }
}

private fun printUserInfo(user: GithubUser) {
    val creationDateTime = DateTimeFormatter.ISO_DATE_TIME.parse(user.createdAt)
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    println("Name: ${user.name}")
    println("Creation: ${dateTimeFormatter.format(creationDateTime)}")
    println("Followers: ${user.followers}")
    println("Following: ${user.following}")
    println("Repos:")
    for (repo in user.repos) println("- $repo")
}