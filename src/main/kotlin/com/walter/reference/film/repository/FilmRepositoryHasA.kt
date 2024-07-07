package com.walter.reference.film.repository

import com.walter.reference.film.dto.FilmPriceSummary
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.JFilm
import org.jooq.generated.tables.JInventory
import org.jooq.generated.tables.daos.FilmDao
import org.jooq.generated.tables.pojos.Film
import org.jooq.impl.DSL.case_
import org.jooq.impl.DSL.inline
import org.jooq.impl.DSL.selectCount
import org.jooq.types.UInteger
import org.jooq.types.UShort
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class FilmRepositoryHasA(
    val dslContext: DSLContext,
    val configuration: Configuration,
    val filmDao: FilmDao = FilmDao(configuration),
) {
    fun findById(id: Long): Film? {
        val uIntegerId = UInteger.valueOf(id)
        return filmDao.fetchOneByJFilmId(uIntegerId)
    }

    fun findByRangeBetween(from: Int, to: Int): List<Film> {
        return filmDao.fetchRangeOfJLength(UShort.valueOf(from), UShort.valueOf(to))
    }

    fun findFilmPriceSummaryByFilmTitle(filmTitle: String): List<FilmPriceSummary> {
        val FILM: JFilm = JFilm.FILM
        val INVENTORY: JInventory = JInventory.INVENTORY

        return dslContext.select(
                FILM.FILM_ID,
                FILM.TITLE,
                FILM.RENTAL_RATE,
                case_()
                    .`when`(FILM.RENTAL_RATE.le(BigDecimal.valueOf(1.0)), inline("Cheap"))
                    .`when`(FILM.RENTAL_RATE.le(BigDecimal.valueOf(3.0)), inline("Moderate"))
                    .otherwise(inline("Expensive")).`as`("price_category"),
                selectCount().from(INVENTORY).where(INVENTORY.FILM_ID.eq(FILM.FILM_ID)).asField<Long>("total_inventory")
            )
            .from(FILM)
            .where(FILM.TITLE.like("%$filmTitle%"))
            .fetchInto(FilmPriceSummary::class.java)
    }
}

