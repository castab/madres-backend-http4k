package madres.backend.health

import org.http4k.core.*
import org.http4k.format.Jackson.auto
import org.http4k.routing.*

fun healthRoutes(): RoutingHttpHandler {
    data class HealthLens(val status: String)
    val healthLens = Body.auto<HealthLens>().toLens()
    return routes(
        "/health" bind Method.GET to {
            Response(Status.OK).with(healthLens of HealthLens("OK"))
        }
    )
}