package com.nicolasmilliard.githubcontributormultiplatform


import com.nicolasmilliard.githubcontributormultiplatform.di.AppScope
import com.nicolasmilliard.githubcontributormultiplatform.github.GithubApiKey
import com.nicolasmilliard.githubcontributormultiplatform.github.GithubComponent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@AppScope
abstract class AppComponent(
  @get:Provides val key: GithubApiKey,
) : GithubComponent {
  companion object
}
