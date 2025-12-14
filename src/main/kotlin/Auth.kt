package madres.backend

import org.http4k.core.Filter
import org.http4k.core.Response
import org.http4k.core.Status

fun requireAuth(expectedToken: String): Filter = Filter { next ->
  { request ->
    val authHeader = request.header("Authorization")
    if (authHeader == expectedToken) {
      next(request)
    } else {
      Response(Status.UNAUTHORIZED)
        .header("Content-Type", "application/json")
        .body("""{"error": "Unauthorized"}""")
    }
  }
}
