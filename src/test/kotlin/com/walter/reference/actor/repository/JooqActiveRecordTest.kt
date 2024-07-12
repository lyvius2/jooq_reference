package com.walter.reference.actor.repository

import org.assertj.core.api.Assertions.assertThat
import org.jooq.DSLContext
import org.jooq.generated.tables.JActor
import org.jooq.generated.tables.records.ActorRecord
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class JooqActiveRecordTest(
    @Autowired val actorRepository: ActorRepository,
    @Autowired val dslContext: DSLContext,
) {
    @Test
    @DisplayName("SELECT 절 예제")
    fun activeRecordSelectTest() {
        // given
        val actorId = 1L

        // when
        val actorRecord: ActorRecord? = actorRepository.findRecordByActorId(actorId)

        // then
        assertThat(actorRecord).hasNoNullFieldsOrProperties()
    }

    @Test
    @DisplayName("ActiveRecord Refresh 예제")
    fun activeRecordRefreshTest() {
        // given
        val actorId = 1L
        val actorRecord: ActorRecord = actorRepository.findRecordByActorId(actorId) ?: throw IllegalStateException()
        actorRecord.firstName = null

        // when
        actorRecord.refresh()
        //actorRecord.refresh(JActor.ACTOR.FIRST_NAME)

        // then
        assertThat(actorRecord.firstName).isNotBlank()
    }

    @Test
    @DisplayName("ActiveRecord Store 예제 - INSERT")
    fun activeRecordStoreInsertTest() {
        // given
        val actorRecord = dslContext.newRecord(JActor.ACTOR)

        // when
        actorRecord.firstName = "Walter"
        actorRecord.lastName = "Model"

        // then
        assertDoesNotThrow { actorRecord.store() }  // actorRecord.insert()
        assertDoesNotThrow { actorRecord.refresh() }
        assertThat(actorRecord.lastUpdate).isNotNull()
    }

    @Test
    @DisplayName("ActiveRecord Store 예제 - UPDATE")
    fun activeRecordStoreUpdateTest() {
        // given
        val actorId = 1L
        val actorRecord: ActorRecord = actorRepository.findRecordByActorId(actorId) ?: throw IllegalStateException()
        val newName = "Steve"

        // when
        actorRecord.firstName = newName

        // then
        assertDoesNotThrow { actorRecord.store() }  // actorRecord.update()
        assertDoesNotThrow { actorRecord.refresh() }
        assertThat(actorRecord.firstName).isEqualTo(newName)
        assertThat(actorRecord.lastUpdate).isNotNull()
    }

    @Test
    @DisplayName("ActiveRecord Delete 예제")
    @Transactional
    fun activeRecordDeleteTest() {
        // given
        val actorRecord = dslContext.newRecord(JActor.ACTOR)
        actorRecord.firstName = "Jack"
        actorRecord.lastName = "Lampard"
        actorRecord.store()

        // when
        val result = actorRecord.delete()

        // then
        assertThat(result).isEqualTo(1)
    }
}