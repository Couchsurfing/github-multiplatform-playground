package com.nicolasmilliard.githubcontributormultiplatform

suspend inline fun <F, T> Mapper<F, T>.map(collection: Collection<F>) = collection.map { map(it) }


inline fun <F, T1, T2> pairMapperOf(
  firstMapper: Mapper<F, T1>,
  secondMapper: Mapper<F, T2>,
): suspend (List<F>) -> List<Pair<T1, T2>> = { from ->
  from.map { value ->
    firstMapper.map(value) to secondMapper.map(value)
  }
}

inline fun <F, T1, T2> pairMapperOf(
  firstMapper: Mapper<F, T1>,
  secondMapper: IndexedMapper<F, T2>,
): suspend (List<F>) -> List<Pair<T1, T2>> = { from ->
  from.mapIndexed { index, value ->
    firstMapper.map(value) to secondMapper.map(index, value)
  }
}
