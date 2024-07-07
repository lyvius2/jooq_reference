package com.walter.reference.film.repository

import com.walter.reference.film.dto.FilmPriceSummary
import com.walter.reference.film.dto.FilmRentalSummary
import com.walter.reference.utils.jooq.JooqListConditionUtil
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.DatePart
import org.jooq.generated.tables.JFilm
import org.jooq.generated.tables.JInventory
import org.jooq.generated.tables.JRental
import org.jooq.generated.tables.daos.FilmDao
import org.jooq.generated.tables.pojos.Film
import org.jooq.impl.DSL
import org.jooq.impl.DSL.avg
import org.jooq.impl.DSL.case_
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.inline
import org.jooq.impl.DSL.select
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

    fun findFilmRentalSummaryByFilmTitle(filmTitle: String): List<FilmRentalSummary> {
        val FILM: JFilm = JFilm.FILM
        val INVENTORY: JInventory = JInventory.INVENTORY
        val RENTAL: JRental = JRental.RENTAL

        val rentalDurationInfoSubquery = select(
                INVENTORY.FILM_ID,
                avg(DSL.localDateTimeDiff(DatePart.DAY, RENTAL.RENTAL_DATE, RENTAL.RETURN_DATE)).`as`("average_rental_duration")
            )
            .from(RENTAL)
            .join(INVENTORY)
            .on(RENTAL.INVENTORY_ID.eq(INVENTORY.INVENTORY_ID))
            .where(RENTAL.RENTAL_DATE.isNotNull())
            .groupBy(INVENTORY.FILM_ID)
            .asTable("rental_duration_info")

        return dslContext.select(
                FILM.FILM_ID,
                FILM.TITLE,
                rentalDurationInfoSubquery.field("average_rental_duration")
            )
            .from(FILM)
            .join(rentalDurationInfoSubquery)
            .on(FILM.FILM_ID.eq(rentalDurationInfoSubquery.field(INVENTORY.FILM_ID)))
            .where(JooqListConditionUtil.containsIfNotBlank(FILM.TITLE, filmTitle))
            .orderBy(field(DSL.name("average_rental_duration")).desc())
            .fetchInto(FilmRentalSummary::class.java)
    }
}

