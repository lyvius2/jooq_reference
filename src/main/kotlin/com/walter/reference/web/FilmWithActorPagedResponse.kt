package com.walter.reference.web

import com.walter.reference.film.dto.FilmWithActor

data class FilmWithActorPagedResponse(
    val page: PagedResponse,
    val filmActorList: List<FilmActorResponse>
) {
    companion object {
        fun from(page: PagedResponse, filmWithActors: List<FilmWithActor>): FilmWithActorPagedResponse {
            return FilmWithActorPagedResponse(page, filmWithActors.stream().map { FilmActorResponse(it) }.toList())
        }
    }

    data class FilmActorResponse(
        val filmTitle: String?,
        val actorFullName: String,
        val filmId: Long,
    ) {
        constructor(filmWithActor: FilmWithActor) : this(
            filmTitle = filmWithActor.getTitle(),
            actorFullName = filmWithActor.getActorFullName(),
            filmId = filmWithActor.getFilmId()
        )
    }
}
