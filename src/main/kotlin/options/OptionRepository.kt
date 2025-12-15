package madres.backend.options

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper

class OptionRepository(
  private val jdbi: Jdbi,
) {
  private val insertStatement =
    """
            INSERT INTO madres.options (
            type, display, description, cost
        )
        VALUES (
            :type, :display, :description, :cost
        );
    """.trimIndent()

  fun insertNewOption(newOption: Option) {
    jdbi.withHandle<Unit, Exception> { handle ->
      handle.createUpdate(insertStatement)
        .bind("type", newOption.type)
        .bind("display", newOption.display)
        .bind("description", newOption.description)
        .bind("cost", newOption.cost)
        .execute()
    }
  }

  private val getOptionsByTypeStatement =
    """
        SELECT type, name, display, description, cost
        FROM madres.options
        WHERE type = :type;
    """.trimIndent()

  fun getOptionsByType(type: Option.Type): List<Option> = jdbi.withHandle<List<Option>, Exception> { handle ->
    handle.createQuery(getOptionsByTypeStatement)
      .bind("type", type.name)
      .map(existingOptionMapper)
      .toList()
  }

  private val existingOptionMapper =
    RowMapper { rs, _ ->
      val type = Option.Type.valueOf(rs.getString("type"))
      val name = rs.getString("name")
      val display = rs.getString("display")
      val description = rs.getString("description")
      val cost = rs.getDouble("cost")
      when (type) {
        Option.Type.APPETIZER -> Option.Appetizer(name, display, description, cost)
        Option.Type.BEVERAGE -> Option.Beverage(name, display, description, cost)
        Option.Type.ENTREE -> Option.Entree(name, display, description, cost)
        Option.Type.MODIFIER -> Option.Modifier(name, display, description, cost)
      }
    }
}
