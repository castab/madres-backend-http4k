package madres.backend.options

import com.fasterxml.jackson.annotation.JsonIgnore

sealed class Option(
  @JsonIgnore
  val type: Type,
  open val name: String,
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
    override val name: String,
    override val display: String,
    override val description: String,
    override val cost: Double,
  ) : Option(Type.APPETIZER, name, display, description, cost)

  data class Beverage(
    override val name: String,
    override val display: String,
    override val description: String,
    override val cost: Double,
  ) : Option(Type.BEVERAGE, name, display, description, cost)

  data class Entree(
    override val name: String,
    override val display: String,
    override val description: String,
    override val cost: Double,
  ) : Option(Type.ENTREE, name, display, description, cost)

  data class Modifier(
    override val name: String,
    override val display: String,
    override val description: String,
    override val cost: Double,
  ) : Option(Type.MODIFIER, name, display, description, cost)
}
