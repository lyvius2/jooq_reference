package com.walter.reference.actor.repository

import com.walter.reference.actor.dto.ActorFilmography
import com.walter.reference.actor.dto.ActorFilmographySearchCondition
import com.walter.reference.actor.dto.ActorUpdateRequest
import com.walter.reference.utils.jooq.JooqListConditionUtil
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.generated.tables.JActor
import org.jooq.generated.tables.JFilm
import org.jooq.generated.tables.JFilmActor
import org.jooq.generated.tables.daos.ActorDao
import org.jooq.generated.tables.pojos.Actor
import org.jooq.generated.tables.pojos.Film
import org.jooq.generated.tables.records.ActorRecord
import org.jooq.impl.DSL
import org.jooq.types.UInteger
import org.springframework.stereotype.Repository

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
            .where(JooqListConditionUtil.inIfNotEmpty(ACTOR.ACTOR_ID, uIntegerIds))
            .fetchInto(Actor::class.java)
    }

    fun findFilmography(condition: ActorFilmographySearchCondition): List<ActorFilmography> {
        val FILM_ACTOR = JFilmActor.FILM_ACTOR
        val FILM = JFilm.FILM

        val actorListMap = dslContext.select(
                ACTOR,
                FILM,
            )
            .from(ACTOR)
            .join(FILM_ACTOR).on(FILM_ACTOR.ACTOR_ID.eq(ACTOR.ACTOR_ID))
            .join(FILM).on(FILM.FILM_ID.eq(FILM_ACTOR.FILM_ID))
            .where(
                JooqListConditionUtil.containsIfNotBlank(ACTOR.FIRST_NAME.concat(" ").concat(ACTOR.LAST_NAME), condition.actorName),
                JooqListConditionUtil.containsIfNotBlank(FILM.TITLE, condition.filmTitle)
            )
            .fetchGroups(
                { record -> record[ACTOR.name, Actor::class.java] },
                { record -> record[FILM.name, Film::class.java] }
            )
        return actorListMap.entries.stream()
            .map { ActorFilmography(it.key, it.value) }
            .toList()
    }

    fun saveByDao(actor: Actor): Actor {
        actorDao.insert(actor)
        return actor
    }

    fun saveByRecord(actor: Actor): ActorRecord {
        val actorRecord: ActorRecord = dslContext.newRecord(ACTOR, actor)
        actorRecord.insert()
        return actorRecord
    }

    fun saveWithReturningKey(actor: Actor): Long? {
        return dslContext.insertInto(
            ACTOR,
            ACTOR.FIRST_NAME,
            ACTOR.LAST_NAME
        ).values(
            actor.firstName,
            actor.lastName
        ).returningResult(ACTOR.ACTOR_ID).fetchOneInto(Long::class.java)
    }

    fun saveWithReturning(actor: Actor): Actor? {
        return dslContext.insertInto(
            ACTOR,
            ACTOR.FIRST_NAME,
            ACTOR.LAST_NAME
        ).values(
            actor.firstName,
            actor.lastName
        ).returning(*ACTOR.fields()).fetchOneInto(Actor::class.java)
    }

    fun bulkInsert(actors: MutableList<Actor>) {
        val rows = actors.stream()
            .map { DSL.row(it.firstName, it.lastName) }
            .toList()

        dslContext.insertInto(
            ACTOR,
            ACTOR.FIRST_NAME,
            ACTOR.LAST_NAME
        ).valuesOfRows(rows).execute()
    }

    fun update(actor: Actor) {
        actorDao.update(actor)
    }

    fun findById(actorId: UInteger?): Actor? {
        return actorDao.findById(actorId)
    }

    fun updateWithDto(id: Long?, request: ActorUpdateRequest): Int {
        return dslContext.update(ACTOR)
            .set(ACTOR.FIRST_NAME, createValidatedSetter(request.firstName, ACTOR.FIRST_NAME))
            .set(ACTOR.LAST_NAME, createValidatedSetter(request.lastName, ACTOR.LAST_NAME))
            .where(ACTOR.ACTOR_ID.eq(UInteger.valueOf(id!!)))
            .execute()
    }

    private fun <T> createValidatedSetter(value: T, field: Field<T>): Field<T> {
        if (value == null) {
            return DSL.noField(field)
        }
        return DSL.`val`(value)
    }
}