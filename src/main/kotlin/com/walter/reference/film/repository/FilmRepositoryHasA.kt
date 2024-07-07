package com.walter.reference.film.repository

import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.FilmDao
import org.jooq.generated.tables.pojos.Film
import org.jooq.types.UInteger
import org.jooq.types.UShort
import org.springframework.stereotype.Repository

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
}

