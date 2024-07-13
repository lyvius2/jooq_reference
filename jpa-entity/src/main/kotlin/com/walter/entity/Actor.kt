package com.walter.entity

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "actor", indexes = [Index(name = "idx_actor_last_name", columnList = "last_name")])
class Actor(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id")
    val id: Long? = null,

    @Column(name = "first_name", nullable = false, length = 45)
    var firstName: String? = null,

    @Column(name = "last_name", nullable = false, length = 45)
    var lastName: String? = null,

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_update", nullable = false)
    var lastUpdate: Instant? = null,
)
