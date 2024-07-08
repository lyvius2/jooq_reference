package com.walter.reference.actor.repository

import org.assertj.core.api.Assertions.assertThat
import org.jooq.generated.tables.pojos.Actor
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
class JooqInsertTest(
    @Autowired val actorRepository: ActorRepository,
) {
    @Test
    @DisplayName("자동 생성된 DAO를 이용한 Insert")
    @Transactional
    fun insertByDaoTest() {
        // given
        val actor: Actor = Actor()
        actor.firstName = "John"
        actor.lastName = "Doe"
        actor.lastUpdate = LocalDateTime.now()

        // when
        val savedActor = actorRepository.saveByDao(actor)

        // then
        assertThat(savedActor).isNotNull
        assertThat(actor).isEqualTo(savedActor)
    }

    @Test
    @DisplayName("ActiveRecord를 통한 Insert")
    @Transactional
    fun insertByActiveRecordTest() {
        // given
        val actor: Actor = Actor()
        actor.firstName = "John"
        actor.lastName = "Doe"
        actor.lastUpdate = LocalDateTime.now()

        // when
        val actorRecord = actorRepository.saveByRecord(actor)

        // then
        assertThat(actorRecord).isNotNull
        assertThat(actorRecord.actorId).isNotNull()
        assertThat(actor.actorId).isNull()
    }

    @Test
    @DisplayName("SQL 실행 후 PK만 반환")
    @Transactional
    fun insertAndReturnPkTest() {
        // given
        val actor: Actor = Actor()
        actor.firstName = "John"
        actor.lastName = "Doe"
        actor.lastUpdate = LocalDateTime.now()

        // when
        val pk = actorRepository.saveWithReturningKey(actor)

        // then
        assertThat(pk).isNotNull
    }

    @Test
    @DisplayName("SQL 실행 후 해당 ROW 반환")
    @Transactional
    fun insertAndReturningTest() {
        // given
        val actor: Actor = Actor()
        actor.firstName = "John"
        actor.lastName = "Doe"
        actor.lastUpdate = LocalDateTime.now()

        // when
        val actorRecord = actorRepository.saveWithReturning(actor)

        // then
        assertThat(actorRecord).isNotNull
    }

    @Test
    @DisplayName("Bulk Insert Sample")
    @Transactional
    fun insertBulkTest() {
        // given
        val actor1: Actor = Actor()
        actor1.firstName = "John"
        actor1.lastName = "Doe"
        actor1.lastUpdate = LocalDateTime.now()

        val actor2: Actor = Actor()
        actor2.firstName = "Jack"
        actor2.lastName = "Kennedy"
        actor2.lastUpdate = LocalDateTime.now()

        val actors = mutableListOf(actor1, actor2)

        // when & then
        assertDoesNotThrow { actorRepository.bulkInsert(actors) }
    }
}