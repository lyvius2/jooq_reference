package com.walter.reference.film.dto

import org.jooq.generated.tables.pojos.Actor
import org.jooq.generated.tables.pojos.Film
import org.jooq.generated.tables.pojos.FilmActor

data class FilmWithActor(
    var film: Film?,
    var filmActor: FilmActor?,
    var actor: Actor?
) {
    fun getTitle(): String {
        return film!!.title ?: ""
    }

    fun getActorFullName(): String {
        return "${actor!!.firstName} ${actor!!.lastName}"
    }

    fun getFilmId(): Long {
        return filmActor!!.filmId!!.toLong()
    }
}
