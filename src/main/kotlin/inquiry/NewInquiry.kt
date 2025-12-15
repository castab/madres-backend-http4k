package madres.backend.inquiry

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import madres.backend.options.Option

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class NewInquiry(
  val name: String,
  val emailAddress: String,
  val phoneNumber: String,
  val guestCount: Int,
  val selections: List<Option>,
  val otherDetails: String,
) {
  val userHash =
    run {
      val name = name.lowercase().trim()
      val emailAddress = emailAddress.lowercase().trim()
      val phoneNumber = phoneNumber.filter { it.isDigit() }
      "$name/$emailAddress/$phoneNumber".hashCode()
    }
}
