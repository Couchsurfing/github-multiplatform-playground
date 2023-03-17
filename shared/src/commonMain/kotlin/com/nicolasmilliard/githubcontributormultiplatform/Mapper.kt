package com.nicolasmilliard.githubcontributormultiplatform

fun interface Mapper<F, T> {
  suspend fun map(from: F): T
}

fun interface IndexedMapper<F, T> {
  suspend fun map(index: Int, from: F): T
}
