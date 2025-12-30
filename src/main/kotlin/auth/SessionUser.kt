package madres.backend.auth

import java.util.UUID

data class SessionUser(
    val userId: UUID,
    val email: String,
)
