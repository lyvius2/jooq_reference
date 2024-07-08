package com.walter.reference.film.dto

import com.walter.reference.film.enum.PriceCategory
import java.math.BigDecimal

data class FilmPriceSummary(
    val filmId: Long,
    val title: String,
    val rentalRate: BigDecimal,
    val priceCategory: PriceCategory,
    val totalInventory: Long,
)
