package madres.backend.options

enum class AppetizerOption(
  override val display: String,
  override val description: String,
) : Option {
  FLAUTAS("Flautas", "Crispy fried rolls of shredded chicken wrapped in a corn tortilla"),
  CORN_CUP("Corn Cup", "Sweet corn in a cup topped with Mexican crema, cheese, and Tajin"),
  FRUIT_CUP("Fruit Cup", "A fresh selection of berries and sliced passion fruits in a cup"),
}
