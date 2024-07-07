package com.walter.reference.film.repository

import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.FilmDao
import org.springframework.stereotype.Repository

@Repository
class FilmRepositoryIsA (
    val dslContext: DSLContext,
    val configuration: Configuration,
) : FilmDao(configuration) {
}

