package com.walter.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Embeddable
class FilmActorId(
    @Column(name = "actor_id")
    val actorId: Long? = null,

    @Column(name = "film_id")
    val filmId: Long? = null
) : Serializable {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false
        val entity = o as FilmActorId
        return this.actorId == entity.actorId && (this.filmId == entity.filmId)
    }

    override fun hashCode(): Int {
        return Objects.hash(actorId, filmId)
    }

    companion object {
        private const val serialVersionUID = -6102408686104668723L
    }
}