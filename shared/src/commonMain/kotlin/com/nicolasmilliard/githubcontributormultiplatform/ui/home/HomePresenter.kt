package com.nicolasmilliard.githubcontributormultiplatform.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import co.touchlab.kermit.Logger
import com.nicolasmilliard.githubcontributormultiplatform.ApiResult.Success
import com.nicolasmilliard.githubcontributormultiplatform.ApiResult.Failure
import com.nicolasmilliard.githubcontributormultiplatform.ApiResult.Failure.ApiFailure
import com.nicolasmilliard.githubcontributormultiplatform.ApiResult.Failure.HttpFailure
import com.nicolasmilliard.githubcontributormultiplatform.ApiResult.Failure.UnknownFailure
import com.nicolasmilliard.githubcontributormultiplatform.ApiResult.Failure.NetworkFailure
import com.nicolasmilliard.githubcontributormultiplatform.github.GithubService
import kotlinx.coroutines.flow.Flow

sealed interface Event {
  data class SelectBreed(val breed: String) : Event
  object FetchAgain : Event
}

data class Model(
  val loading: Boolean,
  val breeds: List<String>,
  val dropdownText: String,
  val currentUrl: String?,
)

@Composable
internal fun HomePresenter(events: Flow<Event>, service: GithubService): Model {
  var breeds: List<String> by remember { mutableStateOf(emptyList()) }
  var currentBreed: String? by remember { mutableStateOf(null) }
  var currentUrl: String? by remember { mutableStateOf(null) }
  var fetchId: Int by remember { mutableStateOf(0) }

  // Grab the list of breeds and sets the current selection to the first in the list.
  // Errors are ignored in this sample.
  LaunchedEffect(Unit) {
    when(val result = service.getRepositoryWatchers("cashapp", "molecule")){
      is Success -> {
        breeds = result.value.map { it.name }
        currentBreed = breeds.first()
        currentUrl = result.value.first().avatarUrl
      }
      is Failure -> when(result){
        is ApiFailure -> Logger.e { "ApiFailure: ${result.error}" }
        is HttpFailure -> Logger.e { "HttpFailure: ${result.error}" }
        is NetworkFailure -> Logger.e(result.error) { "NetworkFailure" }
        is UnknownFailure -> Logger.e(result.error) { "UnknownFailure" }
      }
    }
  }


  // Handle UI events.
  LaunchedEffect(Unit) {
    events.collect { event ->
      when (event) {
        is Event.SelectBreed -> currentBreed = event.breed
        Event.FetchAgain -> fetchId++ // Incrementing fetchId will load another random image URL.
      }
    }
  }

  return Model(
    loading = currentBreed == null,
    breeds = breeds,
    dropdownText = currentBreed ?: "Select breed",
    currentUrl = currentUrl,
  )
}
