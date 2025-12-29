package madres.backend.configurations

import com.fasterxml.jackson.module.kotlin.readValue
import madres.backend.common.objectMapper
import org.jdbi.v3.core.Jdbi

class ConfigurationRepository(
    private val jdbi: Jdbi
) {
    private val getConfigurationByKeyStatement =
        """
            SELECT value FROM madres.configurations WHERE key = :key;
        """.trimIndent()

    fun getConfigurationByKey(key: Configuration.ConfigurationKey): Configuration? {
        return jdbi.withHandle<Configuration?, Exception> { handle ->
            handle.select(getConfigurationByKeyStatement)
                .bind("key", key)
                .map { rs, _ ->
                    val value = rs.getString("value")
                    when (key) {
                        Configuration.ConfigurationKey.BASE_RATE -> objectMapper.readValue<Configuration.BaseRate>(value)
                    }
                }
                .one()
        }
    }
}