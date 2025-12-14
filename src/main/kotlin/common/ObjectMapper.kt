package madres.backend.common

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.http4k.core.ContentType
import org.http4k.core.Response
import org.http4k.core.Status

val objectMapper: ObjectMapper = ObjectMapper().apply {
    // Register the Kotlin module for better Kotlin support
    registerKotlinModule()
    registerModule(JavaTimeModule())

    // Set the naming strategy for properties to SNAKE_CASE
    // PropertyNamingStrategy.SNAKE_CASE is deprecated, use PropertyNamingStrategies.SNAKE_CASE
    propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE

    // Serialization Features
    enable(SerializationFeature.INDENT_OUTPUT) // Pretty-print JSON for readability
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Write dates as ISO-8601 strings, not timestamps

    // Deserialization Features
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // Ignore unknown JSON properties during deserialization
    enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY) // Handle single values as arrays for collections
    enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT) // Treat empty arrays as null for objects
}

fun Any.toJsonResponse(status: Status = Status.OK) = Response(status)
    .header("Content-Type", ContentType.APPLICATION_JSON.value)
    .body(objectMapper.writeValueAsString(this))
