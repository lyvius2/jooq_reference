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
@Table(
    name = "customer",
    indexes = [
        Index(name = "idx_fk_store_id", columnList = "store_id"),
        Index(name = "idx_last_name", columnList = "last_name"),
        Index(name = "idx_fk_address_id", columnList = "address_id")
    ]
)
class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    var store: Store? = null,

    @Column(name = "first_name", nullable = false, length = 45)
    var firstName: String? = null,

    @Column(name = "last_name", nullable = false, length = 45)
    var lastName: String? = null,

    @Column(name = "email", length = 50)
    var email: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    var address: Address? = null,

    @ColumnDefault("1")
    @Column(name = "active", nullable = false)
    var active: Boolean = false,

    @Column(name = "create_date", nullable = false)
    var createDate: Instant? = null,

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_update")
    var lastUpdate: Instant? = null,
)