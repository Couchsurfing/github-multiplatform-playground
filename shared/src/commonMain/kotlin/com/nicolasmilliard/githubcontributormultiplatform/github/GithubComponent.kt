package com.nicolasmilliard.githubcontributormultiplatform.github

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.nicolasmilliard.githubcontributormultiplatform.di.AppScope
import me.tatarka.inject.annotations.Provides

interface GithubComponent {
  @AppScope
  @Provides
  fun apolloClient(tokens: GithubTokenRepository): ApolloClient = ApolloClient.Builder()
    .serverUrl("https://api.github.com/graphql")
    .addHttpInterceptor(
      object : HttpInterceptor {
        override suspend fun intercept(
          request: HttpRequest,
          chain: HttpInterceptorChain,
        ): HttpResponse {
          val token = tokens.getToken()
          val newRequest = request.newBuilder().addHeader("Authorization", "Bearer $token").build()
          return chain.proceed(newRequest)
        }
      },
    ).build()

  val GithubServiceGraphQl.bind: GithubService
    @Provides get() = this
}



