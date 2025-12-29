package madres.backend.options

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper

class OptionRepository(
  private val jdbi: Jdbi,
) {
  private val insertStatement =
    """
            INSERT INTO madres.options (
            type, pricing_basis, name, display, description, price, active
        )
        VALUES (
            :type, :pricingBasis, :name, :display, :description, :price, :active
        );
    """.trimIndent()

  fun insertNewOption(newOption: Option) {
    jdbi.withHandle<Unit, Exception> { handle ->
      handle.createUpdate(insertStatement)
        .bind("type", newOption.type)
        .bind("pricingBasis", newOption.pricingBasis)
        .bind("name", newOption.name)
        .bind("display", newOption.display)
        .bind("description", newOption.description)
        .bind("price", newOption.price)
        .bind("active", newOption.active)
        .execute()
    }
  }

  private val getActiveOptionsByTypeStatement =
    """
        SELECT type, pricing_basis, name, display, description, price, active
        FROM madres.options
        WHERE type = :type
        AND active = true;
    """.trimIndent()

  fun getActiveOptionsByType(type: Option.Type): List<Option> = jdbi.withHandle<List<Option>, Exception> { handle ->
    handle.createQuery(getActiveOptionsByTypeStatement)
      .bind("type", type.name)
      .map(existingOptionMapper)
      .toList()
  }

  private val existingOptionMapper =
    RowMapper { rs, _ ->
      val type = Option.Type.valueOf(rs.getString("type"))
      val pricingBasis = Option.PricingBasis.valueOf(rs.getString("pricing_basis"))
      val name = rs.getString("name")
      val display = rs.getString("display")
      val description = rs.getString("description")
      val price = rs.getDouble("price")
      val active = rs.getBoolean("active")
      when (type) {
        Option.Type.APPETIZER -> Option.Appetizer(name, display, description, price, active)
        Option.Type.BEVERAGE -> Option.Beverage(name, display, description, price, active)
        Option.Type.ENTREE -> Option.Entree(name, display, description, price, active)
        Option.Type.MODIFIER -> Option.Modifier(pricingBasis, name, display, description, price, active)
      }
    }
}
