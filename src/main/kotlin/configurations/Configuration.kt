package madres.backend.configurations

import madres.backend.common.objectMapper

sealed class Configuration(
    open val key: ConfigurationKey,
    open val description: String,
) {
    enum class ConfigurationKey {
        BASE_RATE
    }

    data class BaseRate(
        val baseRate: Double,
    ): Configuration(
        ConfigurationKey.BASE_RATE,
        "The base per guest rate used for price estimations."
    )
}
