package com.nicolasmilliard.githubcontributormultiplatform.github

import me.tatarka.inject.annotations.Inject

typealias GithubApiKey = String
@Inject
class GithubTokenRepository(val token: GithubApiKey) {

  suspend fun getToken(): String {
    return token
  }
}
