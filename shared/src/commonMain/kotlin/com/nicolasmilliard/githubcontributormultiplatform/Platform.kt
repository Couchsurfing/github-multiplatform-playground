package com.nicolasmilliard.githubcontributormultiplatform

interface Platform {
  val name: String
}

expect fun getPlatform(): Platform
