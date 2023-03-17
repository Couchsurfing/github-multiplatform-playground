package com.nicolasmilliard.githubcontributormultiplatform

import android.app.Application
import android.content.Context
import com.nicolasmilliard.githubcontributormultiplatform.android.BuildConfig
import com.nicolasmilliard.githubcontributormultiplatform.di.AppScope
import com.nicolasmilliard.githubcontributormultiplatform.github.GithubApiKey
import com.nicolasmilliard.githubcontributormultiplatform.github.GithubComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@AppScope
abstract class AppComponent(
  @get:Provides val application: Application,
  @get:Provides val key: GithubApiKey,
) : GithubComponent {
  companion object {
    private var instance: AppComponent? = null

    /**
     * Get a singleton instance of [ApplicationComponent].
     */
    fun getInstance(context: Context) = instance ?: AppComponent::class.create(
      context.applicationContext as Application, BuildConfig.GITHUB_KEY
    ).also { instance = it }
  }
}
