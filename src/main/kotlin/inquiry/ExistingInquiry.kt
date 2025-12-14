package madres.backend.inquiry

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.Instant
import java.util.UUID

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ExistingInquiry(
  val id: UUID,
  val name: String,
  val emailAddress: String,
  val phoneNumber: String,
  val guestCount: Int,
  val selections: Map<String, List<String>>,
  val otherDetails: String,
  val acknowledgedTs: Instant?,
  val acknowledged: Boolean,
  val reviewedTs: Instant?,
  val reviewed: Boolean,
  val createdTs: Instant,
  val updatedTs: Instant,
)
