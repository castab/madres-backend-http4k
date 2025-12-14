package madres.backend.options

enum class ModifierOption(
  override val display: String,
  override val description: String,
) : Option {
  BUFFET_STYLE("Buffet Style", "Buffet style service offering"),
  OUTSIDE_SERVICE_AREA("Outside Typical Service Area", "For events outside the greater Fresno/Madera area"),
}
