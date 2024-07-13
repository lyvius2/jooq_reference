package com.walter.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.Lob
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.math.BigDecimal
import java.time.Instant


@Entity
@Table(
    name = "film",
    indexes = [
        Index(name = "idx_title", columnList = "title"), 
        Index(name = "idx_fk_language_id", columnList = "language_id"), 
        Index(name = "idx_fk_original_language_id", columnList = "original_language_id")
    ]
)
class Film(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    val id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String? = null,

    @Column(name = "description")
    var description: String? = null,

    @Column(name = "release_year")
    var releaseYear: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    var language: Language? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_language_id")
    var originalLanguage: Language? = null,

    @Column(name = "rental_duration")
    var rentalDuration: Short? = null,

    @ColumnDefault("4.99")
    @Column(name = "rental_rate", nullable = false, precision = 4, scale = 2)
    var rentalRate: BigDecimal? = null,

    @Column(name = "length")
    var length: Int? = null,

    @ColumnDefault("19.99")
    @Column(name = "replacement_cost", nullable = false, precision = 5, scale = 2)
    var replacementCost: BigDecimal? = null,

    @Column(name = "rating")
    var rating: String? = null,

    @Lob
    @Column(name = "special_features")
    var specialFeatures: String? = null,

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_update", nullable = false)
    var lastUpdate: Instant? = null,
)