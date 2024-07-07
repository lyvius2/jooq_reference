package com.walter.reference.film

import org.jooq.DSLContext
import org.jooq.generated.tables.JFilm
import org.jooq.generated.tables.pojos.Film
import org.jooq.types.UInteger
import org.springframework.stereotype.Repository

@Repository
class FilmRepository(
    val dslContext: DSLContext,
) {
    private val FILM: JFilm = JFilm.FILM

    fun findById(id: Long): Film? {
        val uIntegerId = UInteger.valueOf(id)
        return dslContext.select(*FILM.fields())
            .from(FILM)
            .where(FILM.FILM_ID.eq(uIntegerId))
            .fetchOneInto(Film::class.java)
    }
}

