package madres.backend.options

sealed class Option(
  val type: Type,
  open val pricingBasis: PricingBasis,
  open val name: String,
  open val display: String,
  open val description: String,
  open val price: Double,
) {
  enum class Type(val display: String, val plural: String) {
    APPETIZER("Appetizer", "Appetizers"),
    BEVERAGE("Beverage", "Beverages"),
    ENTREE("Entree", "Entrees"),
    MODIFIER("Modifier", "Modifiers"),
  }

  enum class PricingBasis {
    PER_GUEST, PER_EVEN
  }

  data class Appetizer(
    override val name: String,
    override val display: String,
    override val description: String,
    override val price: Double,
  ) : Option(Type.APPETIZER, PricingBasis.PER_GUEST, name, display, description, price)

  data class Beverage(
    override val name: String,
    override val display: String,
    override val description: String,
    override val price: Double,
  ) : Option(Type.BEVERAGE,  PricingBasis.PER_GUEST, name, display, description, price)

  data class Entree(
    override val name: String,
    override val display: String,
    override val description: String,
    override val price: Double,
  ) : Option(Type.ENTREE, PricingBasis.PER_GUEST, name, display, description, price)

  data class Modifier(
    override val pricingBasis: PricingBasis,
    override val name: String,
    override val display: String,
    override val description: String,
    override val price: Double,
  ) : Option(Type.MODIFIER, pricingBasis, name, display, description, price)
}
