package com.walter.reference.film.repository

import com.walter.reference.film.dto.FilmWithActor
import com.walter.reference.film.dto.SimpleFilmInfo
import org.jooq.DSLContext
import org.jooq.generated.tables.JActor
import org.jooq.generated.tables.JFilm
import org.jooq.generated.tables.JFilmActor
import org.jooq.generated.tables.pojos.Actor
import org.jooq.generated.tables.pojos.Film
import org.jooq.generated.tables.pojos.FilmActor
import org.springframework.stereotype.Repository

@Repository
class FilmRepository(
    val dslContext: DSLContext,
) {
    private val FILM: JFilm = JFilm.FILM

    fun findById(id: Long): Film? {
        return dslContext.select(*FILM.fields())
            .from(FILM)
            .where(FILM.FILM_ID.eq(id))
            .fetchOneInto(Film::class.java)
    }

    fun findSimpleFilmInfoById(id: Long): SimpleFilmInfo? {
        return dslContext.select(
                FILM.FILM_ID,
                FILM.TITLE,
                FILM.DESCRIPTION)
            .from(FILM)
            .where(FILM.FILM_ID.eq(id))
            .fetchOneInto(SimpleFilmInfo::class.java)
    }

    fun findFilmWithActorList(page: Long, pageSize: Long): List<FilmWithActor> {
        val FILM_ACTOR: JFilmActor = JFilmActor.FILM_ACTOR
        val ACTOR: JActor = JActor.ACTOR

        return dslContext.select(
                *FILM.fields(),
                *FILM_ACTOR.fields(),
                *ACTOR.fields())
            .from(FILM)
            .join(FILM_ACTOR)
                .on(FILM_ACTOR.FILM_ID.eq(FILM.FILM_ID))
            .join(ACTOR)
                .on(ACTOR.ACTOR_ID.eq(FILM_ACTOR.ACTOR_ID))
            .offset((page - 1) * pageSize)
            .limit(pageSize)
            .fetch()
            .map { record -> FilmWithActor(
                film = record.into(FILM).into(Film::class.java),
                filmActor = record.into(FILM_ACTOR).into(FilmActor::class.java),
                actor = record.into(ACTOR).into(Actor::class.java)
            ) }
    }
}

