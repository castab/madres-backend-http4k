package madres.backend.options

enum class BeverageOption(
    override val display: String,
    override val description: String,
): Option {
    HORCHATA("Horchata", "Sweet rice-based Mexican drink"),
    INFUSED_WATER("Infused Water", "Fresh fruit steeped in iced water"),
    BOTTLED_SODA("Bottled Soda", "Bottled soda imported from Mexico")
}