package com.nicolasmilliard.githubcontributormultiplatform

import kotlin.jvm.JvmOverloads
import kotlin.reflect.KClass
import kotlinx.collections.immutable.toImmutableMap

/**
 * Represents a result from a traditional HTTP API. [ApiResult] has two sealed subtypes: [Success]
 * and [Failure]. [Success] is typed to [T] with no error type and [Failure] is typed to [E] with no
 * success type.
 *
 * [Failure] in turn is represented by four sealed subtypes of its own: [Failure.NetworkFailure],
 * [Failure.ApiFailure], [Failure.HttpFailure], and [Failure.UnknownFailure]. This allows for simple
 * handling of results through a consistent, non-exceptional flow via sealed `when` branches.
 *
 * ```
 * when (val result = myApi.someEndpoint()) {
 *   is Success -> doSomethingWith(result.response)
 *   is Failure -> when (result) {
 *     is NetworkFailure -> showError(result.error)
 *     is HttpFailure -> showError(result.code)
 *     is ApiFailure -> showError(result.error)
 *     is UnknownError -> showError(result.error)
 *   }
 * }
 * ```
 *
 * Usually, user code for this could just simply show a generic error message for a [Failure] case,
 * but a sealed class is exposed for more specific error messaging.
 */
public sealed interface ApiResult<out T : Any, out E : Any> {
  /** A successful result with the data available in [response]. */
  public class Success<T : Any>
  internal constructor(public val value: T, tags: Map<KClass<*>, Any>) : ApiResult<T, Nothing> {

    /** Extra metadata associated with the result such as original requests, responses, etc. */
    internal val tags: Map<KClass<*>, Any> = tags

    /** Returns a new copy of this with the given [tags]. */
    public fun withTags(tags: Map<KClass<*>, Any>): Success<T> {
      return Success(value, tags)
    }
  }

  /** Represents a failure of some sort. */
  public sealed interface Failure<E : Any> : ApiResult<Nothing, E> {

    /**
     * A network failure caused by a given [error]. This error is opaque, as the actual type could
     * be from a number of sources (connectivity, etc). This event is generally considered to be a
     * non-recoverable and should be used as signal or logging before attempting to gracefully
     * degrade or retry.
     */
    public class NetworkFailure
    internal constructor(public val error: Exception, tags: Map<KClass<*>, Any>) :
      Failure<Nothing> {

      /** Extra metadata associated with the result such as original requests, responses, etc. */
      internal val tags: Map<KClass<*>, Any> = tags.toImmutableMap()

      /** Returns a new copy of this with the given [tags]. */
      public fun withTags(tags: Map<KClass<*>, Any>): NetworkFailure {
        return NetworkFailure(error, tags.toImmutableMap())
      }
    }

    /**
     * An unknown failure caused by a given [error]. This error is opaque, as the actual type could
     * be from a number of sources (serialization issues, etc). This event is generally considered
     * to be a non-recoverable and should be used as signal or logging before attempting to
     * gracefully degrade or retry.
     */
    public class UnknownFailure
    internal constructor(public val error: Throwable, tags: Map<KClass<*>, Any>) :
      Failure<Nothing> {

      /** Extra metadata associated with the result such as original requests, responses, etc. */
      internal val tags: Map<KClass<*>, Any> = tags.toImmutableMap()

      /** Returns a new copy of this with the given [tags]. */
      public fun withTags(tags: Map<KClass<*>, Any>): UnknownFailure {
        return UnknownFailure(error, tags.toImmutableMap())
      }
    }

    /**
     * An HTTP failure. This indicates a 4xx or 5xx response. The [code] is available for reference.
     *
     * @property code The HTTP status code.
     * @property error An optional [error][E]. This would be from the error body of the response.
     */
    public class HttpFailure<E : Any>
    internal constructor(public val code: Int, public val error: E?, tags: Map<KClass<*>, Any>) :
      Failure<E> {

      /** Extra metadata associated with the result such as original requests, responses, etc. */
      internal val tags: Map<KClass<*>, Any> = tags.toImmutableMap()

      /** Returns a new copy of this with the given [tags]. */
      public fun withTags(tags: Map<KClass<*>, Any>): HttpFailure<E> {
        return HttpFailure(code, error, tags.toImmutableMap())
      }
    }

    /**
     * An API failure. This indicates a 2xx response where [ApiException] was thrown during response
     * body conversion.
     *
     * An [ApiException], the [error] property will be best-effort populated with the value of the
     * [ApiException.error] property.
     *
     * @property error An optional [error][E].
     */
    public class ApiFailure<E : Any>
    internal constructor(public val error: E?, tags: Map<KClass<*>, Any>) : Failure<E> {

      /** Extra metadata associated with the result such as original requests, responses, etc. */
      internal val tags: Map<KClass<*>, Any> = tags.toImmutableMap()

      /** Returns a new copy of this with the given [tags]. */
      public fun withTags(tags: Map<KClass<*>, Any>): ApiFailure<E> {
        return ApiFailure(error, tags.toImmutableMap())
      }
    }
  }

  public companion object {
    private const val OK = 200
    private val HTTP_SUCCESS_RANGE = OK..299
    private val HTTP_FAILURE_RANGE = 400..599

    /** Returns a new [Success] with given [value]. */
    public fun <T : Any> success(value: T): Success<T> = Success(value, emptyMap())

    /** Returns a new [HttpFailure] with given [code] and optional [error]. */
    @JvmOverloads
    public fun <E : Any> httpFailure(
      code: Int,
      error: E? = null,
    ): Failure.HttpFailure<E> {
      checkHttpFailureCode(code)
      return Failure.HttpFailure(code, error, emptyMap())
    }

    /** Returns a new [ApiFailure] with given [error]. */
    @JvmOverloads
    public fun <E : Any> apiFailure(error: E? = null): Failure.ApiFailure<E> =
      Failure.ApiFailure(error, emptyMap())

    /** Returns a new [NetworkFailure] with given [error]. */
    public fun networkFailure(error: Exception): Failure.NetworkFailure =
      Failure.NetworkFailure(error, emptyMap())

    /** Returns a new [UnknownFailure] with given [error]. */
    public fun unknownFailure(error: Throwable): Failure.UnknownFailure =
      Failure.UnknownFailure(error, emptyMap())

    internal fun checkHttpFailureCode(code: Int) {
      require(code !in HTTP_SUCCESS_RANGE) {
        "Status code '$code' is a successful HTTP response. If you mean to use a $OK code + error " +
          "string to indicate an API error, use the ApiResult.apiFailure() factory."
      }
      require(code in HTTP_FAILURE_RANGE) {
        "Status code '$code' is not a HTTP failure response. Must be a 4xx or 5xx code."
      }
    }
  }
}

