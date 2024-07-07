package com.walter.reference.actor.repository

import org.jooq.Condition
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.TableField
import org.jooq.generated.tables.JActor
import org.jooq.generated.tables.daos.ActorDao
import org.jooq.generated.tables.pojos.Actor
import org.jooq.generated.tables.records.ActorRecord
import org.jooq.impl.DSL
import org.jooq.types.UInteger
import org.springframework.stereotype.Repository
import org.springframework.util.CollectionUtils

@Repository
class ActorRepository(
    val dslContext: DSLContext,
    val configuration: Configuration,
) {
    private val actorDao: ActorDao = ActorDao(configuration)
    private val ACTOR: JActor = JActor.ACTOR

    fun findByFirstAndLastName(firstName: String, lastName: String): List<Actor> {
        return dslContext.selectFrom(ACTOR)
            .where(
                ACTOR.LAST_NAME.eq(lastName),
                ACTOR.FIRST_NAME.eq(firstName)
            )
            .fetchInto(Actor::class.java)
    }

    fun findByFirstOrLastName(firstName: String, lastName: String): List<Actor> {
        return dslContext.selectFrom(ACTOR)
            .where(
                ACTOR.LAST_NAME.eq(lastName).or(ACTOR.FIRST_NAME.eq(firstName))
            )
            .fetchInto(Actor::class.java)
    }

    fun findByActorIdIn(ids: List<Long>?): List<Actor> {
        var uIntegerIds: MutableList<UInteger>? = null
        if (ids != null) {
            uIntegerIds = ids.stream()
                .map { UInteger.valueOf(it) }
                .toList()
        }
        return dslContext.selectFrom(ACTOR)
            .where(inIfNotEmpty(ACTOR.ACTOR_ID, uIntegerIds))
            .fetchInto(Actor::class.java)
    }

    private fun inIfNotEmpty(actorId: TableField<ActorRecord, UInteger?>, uIntegerIds: List<UInteger>?): Condition {
        if (CollectionUtils.isEmpty(uIntegerIds)) {
            return DSL.noCondition()
        }
        return actorId.`in`(uIntegerIds)
    }

}