package madres.backend.options

import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.then
import org.http4k.core.with
import org.http4k.filter.CachingFilters
import org.http4k.format.Jackson.auto
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.time.Duration

fun menuOptionRoutes(): RoutingHttpHandler {
    data class Option(
        val name: String,
        val display: String,
        val description: String
    )

    val optionsLens = Body.auto<List<Option>>().toLens()
    val cacheFilter = CachingFilters.CacheResponse.MaxAge(
        maxAge = Duration.ofHours(1)
    )

    fun <T> handleOptions(entries: Array<T>): Response where T : Enum<T>, T : madres.backend.options.Option {
        val options = entries.map {
            Option(it.name, it.display, it.description)
        }
        return Response(Status.OK).with(optionsLens of options)
    }

    return routes(
        "/options/entree" bind Method.GET to cacheFilter.then {
            handleOptions(EntreeOption.entries.toTypedArray())
        },
        "/options/beverage" bind Method.GET to cacheFilter.then {
            handleOptions(BeverageOption.entries.toTypedArray())
        },
        "/options/appetizer" bind Method.GET to cacheFilter.then {
            handleOptions(AppetizerOption.entries.toTypedArray())
        },
        "/options/modifier" bind Method.GET to cacheFilter.then {
            handleOptions(ModifierOption.entries.toTypedArray())
        }
    )
}