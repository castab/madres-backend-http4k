package madres.backend.auth

import java.util.UUID

data class UserAuthRow(
    val id: UUID,
    val email: String,
    val passwordHash: String,
)