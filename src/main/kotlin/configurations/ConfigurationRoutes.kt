package madres.backend.configurations

import madres.backend.common.toJsonResponse
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

fun configurationRoutes(repository: ConfigurationRepository): RoutingHttpHandler {
    val configurationRoutes = Configuration.ConfigurationKey.entries.map { key ->
        "/config/${key.name.lowercase()}" bind Method.GET to { _ ->
            repository.getConfigurationByKey(key)?.toJsonResponse() ?: Response(Status.NOT_FOUND)
        }
    }
    return routes(configurationRoutes)
}