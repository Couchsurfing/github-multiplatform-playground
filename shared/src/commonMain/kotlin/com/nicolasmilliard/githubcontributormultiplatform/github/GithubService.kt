package com.nicolasmilliard.githubcontributormultiplatform.github

import com.nicolasmilliard.githubcontributormultiplatform.ApiResult


interface GithubService {
  suspend fun getRepositoryWatchers(owner: String, name: String): ApiResult<List<Watchers>, String>
}

data class Watchers(
  val id: String,
  val avatarUrl: String?,
  val name: String,
)
