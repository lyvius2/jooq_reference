package com.walter.reference.film.dto

import java.math.BigDecimal

data class FilmRentalSummary(
    val filmId: Long,
    val title: String,
    val averageRentalDuration: BigDecimal,
)
