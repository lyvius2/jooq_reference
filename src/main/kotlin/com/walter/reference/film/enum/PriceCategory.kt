package com.walter.reference.film.enum

enum class PriceCategory(
    val code: String,
) {
    CHEAP("Cheap"),
    MODERATE("Moderate"),
    EXPENSIVE("Expensive");

    companion object {
        @JvmStatic
        fun findByCode(code: String): PriceCategory? {
            return entries.find { it.code == code }
        }
    }
}