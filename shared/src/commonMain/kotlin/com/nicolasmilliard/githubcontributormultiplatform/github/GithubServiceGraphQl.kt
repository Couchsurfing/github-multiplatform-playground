package com.nicolasmilliard.githubcontributormultiplatform.github

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.exception.ApolloHttpException
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.nicolasmilliard.githubcontributormultiplatform.ApiResult
import me.tatarka.inject.annotations.Inject

@Inject
class GithubServiceGraphQl(val client: Lazy<ApolloClient>) : GithubService {

  override suspend fun getRepositoryWatchers(
    owner: String,
    name: String,
  ): ApiResult<List<Watchers>, String> {
    return try {
      val response = client.value.query(RepositoryWatchersQuery(name = name, owner = owner, first = 10)).execute()
      if (response.hasErrors()) {
        return ApiResult.apiFailure(response.errors.toString())
      } else {
        val watchers = response.data!!.repository!!.watchers.nodes!!.map {
          Watchers(id = it!!.id, name = it.name!!, avatarUrl = it.avatarUrl.toString())
        }
        return ApiResult.success(watchers)
      }
    } catch (exception: ApolloHttpException) {
      ApiResult.httpFailure(exception.statusCode, exception.message)
    } catch (exception: ApolloNetworkException) {
      ApiResult.networkFailure(exception)
    } catch (exception: ApolloException) {
      ApiResult.unknownFailure(exception)
    }
  }

}
