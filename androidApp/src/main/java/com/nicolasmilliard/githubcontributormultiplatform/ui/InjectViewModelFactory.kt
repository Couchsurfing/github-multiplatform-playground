package com.nicolasmilliard.githubcontributormultiplatform.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nicolasmilliard.githubcontributormultiplatform.ui.home.HomeViewModel
import me.tatarka.inject.annotations.Inject

@Suppress("UNCHECKED_CAST")
@Inject
class InjectViewModelFactory(private val homeViewModel: () -> HomeViewModel): ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
      return homeViewModel() as T
    }
    throw UnsupportedOperationException()
  }

}
