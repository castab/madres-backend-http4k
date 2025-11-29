package madres.backend.options

enum class EntreeOption(
    override val display: String,
    override val description: String,
): Option {
    ASADA("Asada", "Grilled marinated steak"),
    ADOBADA("Adobada", "Spicy and sweet marinated pork"),
    POLLO("Chicken", "Chopped marinated chicken"),
    CHORIZO("Chorizo", "Spicy Mexican pork sausage"),
    LENGUA("Lengua", "Tender beef tongue"),
    VEGGIE("Veggie", "Fresh grilled vegetables")
}