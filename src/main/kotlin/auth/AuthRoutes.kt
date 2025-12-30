package madres.backend.auth

import com.fasterxml.jackson.module.kotlin.readValue
import madres.backend.common.objectMapper
import madres.backend.common.toJsonResponse
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import java.util.UUID

data class AuthRequest(val email: String)
data class AuthResponse(
    val id: UUID,
    val email: String,
    val passwordHash: String,
)
data class StartSessionRequest(val userId: UUID)
data class StartSessionResponse(val sessionId: UUID)
data class SessionUserResponse(
    val userId: UUID,
    val email: String,
)


fun authRoutes(repository: AuthRepository): RoutingHttpHandler {
    val authRequest = "/auth" bind Method.POST to { request ->
        val authRequest = runCatching { objectMapper.readValue<AuthRequest>(request.bodyString()) }
            .getOrNull()
        if (authRequest == null || authRequest.email.isBlank()) {
            Response(Status.BAD_REQUEST)
        } else {
            val user = repository.findUserByEmail(authRequest.email)?.let {
                AuthResponse(
                    id = it.id,
                    email = it.email,
                    passwordHash = it.passwordHash
                )
            }
            user?.toJsonResponse() ?: Response(Status.NOT_FOUND)
        }
    }

    val newSession = "/sessions" bind Method.POST to { request ->
        val sessionRequest = runCatching { objectMapper.readValue<StartSessionRequest>(request.bodyString()) }
            .getOrNull()
        if (sessionRequest == null) {
            Response(Status.BAD_REQUEST)
        } else {
            StartSessionResponse(repository.startNewSession(sessionRequest.userId)).toJsonResponse()
        }
    }

    val existingSession = "/sessions/{id}" bind Method.GET to handler@{ request ->
        val sessionIdParam = request.path("id") ?: return@handler Response(Status.BAD_REQUEST)
        val sessionId = runCatching { UUID.fromString(sessionIdParam) }
            .getOrNull() ?: return@handler Response(Status.BAD_REQUEST)
        val sessionUser = repository.getSessionUser(sessionId) ?: return@handler Response(Status.UNAUTHORIZED)
        SessionUserResponse(
            userId = sessionId,
            email = sessionUser.email
        ).toJsonResponse()
    }

    val deleteSession = "sessions/{id}" bind Method.DELETE to handler@{ request ->
        val sessionIdParam = request.path("id") ?: return@handler Response(Status.BAD_REQUEST)
        val sessionId = runCatching { UUID.fromString(sessionIdParam) }
            .getOrNull() ?: return@handler Response(Status.BAD_REQUEST)
        repository.deleteSession(sessionId)
        Response(Status.NO_CONTENT)
    }

    return routes(authRequest, newSession, existingSession, deleteSession)
}