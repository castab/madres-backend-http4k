package madres.backend.inquiry

import com.fasterxml.jackson.module.kotlin.readValue
import madres.backend.common.objectMapper
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper
import java.util.UUID

class InquiryRepository(
    private val jdbi: Jdbi
) {
    private val existsStatement = """
        SELECT EXISTS (
          SELECT 1 FROM madres.inquiries
          WHERE user_hash = :userHash
        );
    """.trimIndent()
    private val insertStatement = """
        INSERT INTO madres.inquiries (
        name, email_address, phone_number, guest_count,
        selections, other_details, user_hash
    )
    VALUES (
        :name, :emailAddress, :phoneNumber, :guestCount,
        :selections::jsonb, :otherDetails, :userHash
    );
    """.trimIndent()
    fun insertNewInquiry(newInquiry: NewInquiry) {
        return jdbi.inTransaction<Unit, Exception> { handle ->
            val exists = handle.createQuery(existsStatement)
                .bind("userHash", newInquiry.userHash)
                .mapTo(Boolean::class.java)
                .firstOrNull() ?: false
            if (exists) throw DuplicateRequestException("User already has an entry waiting to be reviewed")
            handle
                .createUpdate(insertStatement)
                .bind("userHash", newInquiry.userHash)
                .bind("name", newInquiry.name)
                .bind("emailAddress", newInquiry.emailAddress)
                .bind("phoneNumber", newInquiry.phoneNumber)
                .bind("guestCount", newInquiry.guestCount)
                .bind("selections", objectMapper.writeValueAsString(newInquiry.selections))
                .bind("otherDetails", newInquiry.otherDetails)
                .execute()
        }
    }

    fun getInquiryById(inquiryId: UUID): ExistingInquiry? {
        return jdbi.withHandle<ExistingInquiry?, Exception> { handle ->
            handle
                .createQuery("SELECT * FROM madres.inquiries where id = :id")
                .bind("id", inquiryId)
                .map(existingInquiryRowMapper)
                .firstOrNull()
        }
    }

    private val existingInquiryRowMapper = RowMapper { rs, ctx ->
        ExistingInquiry(
            id = UUID.fromString(rs.getString("id")),
            name = rs.getString("name"),
            emailAddress = rs.getString("email_address"),
            phoneNumber = rs.getString("phone_number"),
            guestCount = rs.getInt("guest_count"),
            selections = objectMapper.readValue(rs.getString("selections")),
            otherDetails = rs.getString("other_details"),
            acknowledgedTs = rs.getTimestamp("acknowledged_ts")?.toInstant(),
            acknowledged = rs.getBoolean("acknowledged"),
            reviewedTs = rs.getTimestamp("reviewed_ts")?.toInstant(),
            reviewed = rs.getBoolean("reviewed"),
            createdTs = rs.getTimestamp("created_ts").toInstant(),
            updatedTs = rs.getTimestamp("updated_ts").toInstant()
        )
    }

    class DuplicateRequestException(message: String): IllegalStateException(message)
}