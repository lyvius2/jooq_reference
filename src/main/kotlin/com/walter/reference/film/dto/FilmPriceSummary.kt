package com.walter.reference.film.dto

import java.math.BigDecimal

data class FilmPriceSummary(
    val filmId: Long,
    val title: String,
    val rentalRate: BigDecimal,
    val priceCategory: String,
    val totalInventory: Long,
)
