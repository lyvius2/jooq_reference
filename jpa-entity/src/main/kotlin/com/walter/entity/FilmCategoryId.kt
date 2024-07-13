package com.walter.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.hibernate.Hibernate
import java.io.Serializable
import java.util.*

@Embeddable
class FilmCategoryId(
    @Column(name = "film_id")
    val filmId: Long? = null,

    @Column(name = "category_id")
    val categoryId: Long? = null
) : Serializable {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false
        val entity = o as FilmCategoryId
        return this.filmId == entity.filmId && (this.categoryId == entity.categoryId)
    }

    override fun hashCode(): Int {
        return Objects.hash(filmId, categoryId)
    }

    companion object {
        private const val serialVersionUID = -3650065536069802683L
    }
}