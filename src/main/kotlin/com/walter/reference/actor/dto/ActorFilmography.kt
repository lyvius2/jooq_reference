package com.walter.reference.actor.dto

import org.jooq.generated.tables.pojos.Actor
import org.jooq.generated.tables.pojos.Film

data class ActorFilmography(
    val actor: Actor,
    val filmList: List<Film>
)
