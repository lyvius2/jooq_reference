package com.walter.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "address", indexes = [Index(name = "idx_fk_city_id", columnList = "city_id")])
class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    val id: Long? = null,

    @Column(name = "address", nullable = false, length = 50)
    var address: String? = null,

    @Column(name = "address2", length = 50)
    var address2: String? = null,

    @Column(name = "district", nullable = false, length = 20)
    var district: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    val city: City? = null,

    @Column(name = "postal_code", length = 10)
    var postalCode: String? = null,

    @Column(name = "phone", nullable = false, length = 20)
    var phone: String? = null,

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_update", nullable = false)
    var lastUpdate: Instant? = null
)