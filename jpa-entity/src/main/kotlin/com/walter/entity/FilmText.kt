package com.walter.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Lob
import jakarta.persistence.Table

@Entity
@Table(
    name = "film_text",
    indexes = [Index(name = "idx_title_description", columnList = "title, description")]
)
class FilmText(
    @Id
    @Column(name = "film_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "title", nullable = false)
    var title: String? = null,

    @Lob
    @Column(name = "description")
    var description: String? = null
)