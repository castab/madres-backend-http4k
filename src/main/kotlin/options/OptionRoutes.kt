package madres.backend.options

import madres.backend.common.toJsonResponse
import org.http4k.core.Method
import org.http4k.core.then
import org.http4k.filter.CachingFilters
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.time.Duration

fun menuOptionRoutes(repository: OptionRepository): RoutingHttpHandler {
  data class Type(
    val name: String,
    val display: String,
    val plural: String,
  )

  val cacheFilter =
    CachingFilters.CacheResponse.MaxAge(
      maxAge = Duration.ofHours(1),
    )

  val optionTypes =
    "/options" bind Method.GET to cacheFilter.then{
      Option.Type.entries.map{
        Type(it.name, it.display, it.plural)
      }.toJsonResponse()
    }

  val base =
    "/options/base" bind Method.GET to cacheFilter.then{
      repository.getBaseRate().toJsonResponse()
    }

  val optionTypeRoutes = Option.Type.entries.map { type ->
    "/options/${type.name.lowercase()}" bind Method.GET to
      cacheFilter.then {
        repository.getActiveOptionsByType(type).toJsonResponse()
      }
  }

  return routes(optionTypeRoutes + optionTypes + base)
}
