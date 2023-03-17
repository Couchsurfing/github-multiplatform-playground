package com.nicolasmilliard.githubcontributormultiplatform.ui.home

import androidx.compose.runtime.Composable
import com.nicolasmilliard.githubcontributormultiplatform.github.GithubService
import com.nicolasmilliard.githubcontributormultiplatform.ui.MoleculeViewModel
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class HomeViewModel(private val service: GithubService) : MoleculeViewModel<Event, Model>() {

  @Composable
  override fun models(events: Flow<Event>): Model {
    return HomePresenter(events, service)
  }
}
