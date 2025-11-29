package madres.backend.inquiry

import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.format.Jackson.auto
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import java.util.UUID

fun inquiryRoutes(repository: InquiryRepository): RoutingHttpHandler {
    val newInquiryLens = Body.auto<NewInquiry>().toLens()
    val existingInquiryLens = Body.auto<ExistingInquiry>().toLens()
    val inquiryIdLens = Body.auto<UUID>().toLens()
    return routes(
        "/inquiry/new" bind Method.POST to { request ->
            val new = newInquiryLens(request)
            val id = repository.insertNewInquiry(new)
            Response(Status.CREATED).with(inquiryIdLens of id)
        },
        "/inquiry/{inquiry_id}" bind Method.GET to handler@{ request ->
            val inquiryIdString = request.path("inquiry_id")
            val inquiryId = try {
                UUID.fromString(inquiryIdString)
            } catch (_: IllegalArgumentException) {
                return@handler Response(Status.BAD_REQUEST).body("Invalid UUID format: $inquiryIdString")
            }
            val inquiry = repository.getInquiryById(inquiryId)
            when {
                inquiry == null -> Response(Status.NOT_FOUND)
                else -> Response(Status.OK).with(existingInquiryLens of inquiry)
            }
        }
    )
}