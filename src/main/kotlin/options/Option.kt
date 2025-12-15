package madres.backend.options

import com.fasterxml.jackson.annotation.JsonIgnore

sealed class Option(
  @JsonIgnore
  val type: Type,
  open val display: String,
  open val description: String,
  open val cost: Double,
) {
  enum class Type {
    APPETIZER,
    BEVERAGE,
    ENTREE,
    MODIFIER,
  }

  data class Appetizer(
    override val display: String,
    override val description: String,
    override val cost: Double,
  ) : Option(Type.APPETIZER, display, description, cost)

  data class Beverage(
    override val display: String,
    override val description: String,
    override val cost: Double,
  ) : Option(Type.BEVERAGE, display, description, cost)

  data class Entree(
    override val display: String,
    override val description: String,
    override val cost: Double,
  ) : Option(Type.ENTREE, display, description, cost)

  data class Modifier(
    override val display: String,
    override val description: String,
    override val cost: Double,
  ) : Option(Type.MODIFIER, display, description, cost)
}
