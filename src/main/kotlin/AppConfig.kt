package madres.backend

data class AppConfig(
  val auth: AuthConfig?,
  val server: ServerConfig,
  val database: DatabaseConfig,
) {
  data class AuthConfig(
    val token: String?,
  )

  data class ServerConfig(
    val port: Int,
  )

  data class DatabaseConfig(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val maximumPoolSize: Int = 4,
    val minimumIdle: Int = 1,
  )
}
