package com.nicolasmilliard.githubcontributormultiplatform.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.nicolasmilliard.githubcontributormultiplatform.AppComponent
import com.nicolasmilliard.githubcontributormultiplatform.ui.home.HomeScreen
import com.nicolasmilliard.githubcontributormultiplatform.ui.home.HomeViewModel
import me.tatarka.inject.annotations.Component

class MainActivity : ComponentActivity() {

  private lateinit var component: MainActivityComponent

  private val homeViewModel: HomeViewModel by viewModels(factoryProducer = { component.viewModelFactory })

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    component = MainActivityComponent::class.create(AppComponent.getInstance(this))
    setContent {
      MyApplicationTheme {
        val model by homeViewModel.models.collectAsState()
        HomeScreen(model = model) { event -> homeViewModel.take(event) }
      }
    }
  }
}

@Component
abstract class MainActivityComponent(@Component val parent: AppComponent) {
  abstract val viewModelFactory: InjectViewModelFactory
}



