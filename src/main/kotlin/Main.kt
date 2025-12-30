package madres.backend

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.PropertySource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.oshai.kotlinlogging.KotlinLogging
import madres.backend.auth.AuthRepository
import madres.backend.auth.authRoutes
import madres.backend.configurations.ConfigurationRepository
import madres.backend.configurations.configurationRoutes
import madres.backend.health.healthRoutes
import madres.backend.inquiry.InquiryRepository
import madres.backend.inquiry.inquiryRoutes
import madres.backend.options.OptionRepository
import madres.backend.options.menuOptionRoutes
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.jdbi.v3.core.Jdbi

fun main() {
  val log = KotlinLogging.logger("Madres Backend")
  val appConfig =
    ConfigLoaderBuilder
      .default()
      .addSource(PropertySource.resource("/application.conf", true))
      .addSource(PropertySource.resource("/application-overrides.conf"))
      .addSource(PropertySource.environment())
      .build()
      .loadConfigOrThrow<AppConfig>()

  val ds =
    run {
      val hikariConfig =
        HikariConfig().apply {
          jdbcUrl = appConfig.database.jdbcUrl
          username = appConfig.database.username
          password = appConfig.database.password
          maximumPoolSize = appConfig.database.maximumPoolSize
          minimumIdle = appConfig.database.minimumIdle
          schema = "madres"
        }
      HikariDataSource(hikariConfig)
    }
  val jdbi = Jdbi.create(ds).apply { installPlugins() }
  val inquiryRepository = InquiryRepository(jdbi)
  val optionRepository = OptionRepository(jdbi)
  val configurationRepository = ConfigurationRepository(jdbi)
  val authRepository = AuthRepository(jdbi)

  val publicRoutes =
    routes(
      healthRoutes(),
    )

  val protectedRoutes =
    run {
      val routes =
        routes(
          menuOptionRoutes(optionRepository),
          inquiryRoutes(inquiryRepository),
          configurationRoutes(configurationRepository),
          authRoutes(authRepository)
        )
      if (appConfig.auth?.token.isNullOrBlank()) {
        log.warn { "No auth.token configured - authentication is DISABLED. This should not be used in production!" }
        routes
      } else {
        log.info { "Authentication enabled for protected routes" }
        requireAuth(appConfig.auth.token).then(routes)
      }
    }

  val allRoutes =
    ServerFilters.GZip().then(
      routes(
        publicRoutes,
        protectedRoutes,
      ),
    )

  val server = allRoutes.asServer(Netty(appConfig.server.port)).start()

  log.info { "Server running on port ${appConfig.server.port}" }

  Runtime.getRuntime().addShutdownHook(
    Thread {
      log.info { "Shutdown signal received, stopping server gracefully..." }
      try {
        server.stop()
        log.info { "Server stopped" }
        ds.close()
        log.info { "Database connections closed" }
      } catch (e: Exception) {
        log.error(e) { "Error during shutdown" }
      }
    },
  )
  Thread.currentThread().join()
}
