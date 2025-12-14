package madres.backend.health

import madres.backend.common.toJsonResponse
import org.http4k.core.Method
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.routes

fun healthRoutes(): RoutingHttpHandler {
  data class HealthStatus(
    val status: String,
  )
  return routes(
    "/health" bind Method.GET to {
      HealthStatus("OK").toJsonResponse()
    },
  )
}
