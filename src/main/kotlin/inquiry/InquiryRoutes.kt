package madres.backend.inquiry

import com.fasterxml.jackson.module.kotlin.readValue
import madres.backend.common.objectMapper
import madres.backend.common.toJsonResponse
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import java.util.UUID

fun inquiryRoutes(repository: InquiryRepository) = routes(
  "/inquiry/new" bind Method.POST to { request ->
    val new = objectMapper.readValue<NewInquiry>(request.bodyString())
    repository.insertNewInquiry(new)
    Response(Status.CREATED)
  },
  "/inquiry/{inquiry_id}" bind Method.GET to handler@{ request ->
    val inquiryIdString = request.path("inquiry_id")
    val inquiryId =
      try {
        UUID.fromString(inquiryIdString)
      } catch (_: IllegalArgumentException) {
        return@handler Response(Status.BAD_REQUEST).body("Invalid UUID format: $inquiryIdString")
      }
    val inquiry = repository.getInquiryById(inquiryId)
    when {
      inquiry == null -> Response(Status.NOT_FOUND)
      else -> inquiry.toJsonResponse()
    }
  },
)
