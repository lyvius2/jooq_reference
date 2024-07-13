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
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(
    name = "rental",
    indexes = [
        Index(name = "idx_fk_inventory_id", columnList = "inventory_id"), 
        Index(name = "idx_fk_customer_id", columnList = "customer_id"), 
        Index(name = "idx_fk_staff_id", columnList = "staff_id")
    ], 
    uniqueConstraints = [
        UniqueConstraint(name = "rental_date", columnNames = ["rental_date", "inventory_id", "customer_id"])
    ]
)
class Rental(
    @Id
    @Column(name = "rental_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "rental_date", nullable = false)
    var rentalDate: Instant? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inventory_id", nullable = false)
    var inventory: Inventory? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    var customer: Customer? = null,

    @Column(name = "return_date")
    var returnDate: Instant? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_id", nullable = false)
    var staff: Staff? = null,

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_update", nullable = false)
    var lastUpdate: Instant? = null,
)