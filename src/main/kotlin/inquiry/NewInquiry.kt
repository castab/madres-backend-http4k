package madres.backend.inquiry

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import madres.backend.options.AppetizerOption
import madres.backend.options.BeverageOption
import madres.backend.options.EntreeOption
import madres.backend.options.ModifierOption
import java.util.EnumSet

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class NewInquiry(
    val name: String,
    val emailAddress: String,
    val phoneNumber: String,
    val guestCount: Int,
    val selections: SelectedOptions,
    val otherDetails: String,
) {
    val userHash = run {
        val name = name.lowercase().trim()
        val emailAddress = emailAddress.lowercase().trim()
        val phoneNumber = phoneNumber.filter { it.isDigit() }
        "$name/$emailAddress/$phoneNumber".hashCode()
    }
    data class SelectedOptions(
        val entrees: EnumSet<EntreeOption>,
        val beverages: EnumSet<BeverageOption>,
        val appetizers: EnumSet<AppetizerOption>,
        val modifiers: EnumSet<ModifierOption>
    )
}