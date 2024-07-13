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
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(
    name = "payment",
    indexes = [
        Index(name = "idx_fk_customer_id", columnList = "customer_id"), 
        Index(name = "idx_fk_staff_id", columnList = "staff_id")
    ]
)
class Payment(
    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    var customer: Customer? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "staff_id", nullable = false)
    var staff: Staff? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_id")
    var rental: Rental? = null,

    @Column(name = "amount", nullable = false, precision = 5, scale = 2)
    var amount: BigDecimal? = null,

    @Column(name = "payment_date", nullable = false)
    var paymentDate: Instant? = null,

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "last_update")
    var lastUpdate: Instant? = null
)