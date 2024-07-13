package com.walter.reference.actor.repository

import com.walter.reference.actor.dto.ActorUpdateRequest
import org.assertj.core.api.Assertions.assertThat
import org.jooq.generated.tables.pojos.Actor
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class JooqUpdateTest(
    @Autowired val actorRepository: ActorRepository,
) {
    @Test
    @DisplayName("Pojo를 사용한 Update")
    @Transactional
    fun updateWithPojo() {
        // given
        val newActor: Actor = Actor()
        newActor.firstName = "Jack"
        newActor.lastName = "Kennedy"
        val actor = actorRepository.saveWithReturning(newActor)

        // when
        actor!!.firstName = "John F."
        actorRepository.update(actor)

        // then
        val updatedActor = actorRepository.findById(actor.actorId)
        assertThat(updatedActor).isNotNull
    }

    @Test
    @DisplayName("일부 필드만 Update - DTO사용")
    @Transactional
    fun updateWithDto() {
        // given
        val newActor: Actor = Actor()
        newActor.firstName = "Jack"
        newActor.lastName = "Kennedy"
        val id = actorRepository.saveWithReturningKey(newActor)
        val request = ActorUpdateRequest("John F.")

        // when
        val updateResult = actorRepository.updateWithDto(id, request)

        // then
        val updatedActor = actorRepository.findById(id)
        assertThat(updatedActor).isNotNull
        assertThat(updatedActor!!.firstName).isEqualTo(request.firstName)
        assertThat(updatedActor.lastName).isEqualTo(newActor.lastName)
    }

    @Test
    @DisplayName("일부 필드만 Update - Record")
    @Transactional
    fun updateWithRecord() {
        // given
        val newActor: Actor = Actor()
        newActor.firstName = "Sherlock"
        newActor.lastName = "Holmes"
        val id = actorRepository.saveWithReturningKey(newActor)
        val request = ActorUpdateRequest("Mycroft")

        // when
        val updateResult = actorRepository.updateWithRecord(id, request)

        // then
        val updatedActor = actorRepository.findById(id)
        assertThat(updatedActor).isNotNull
        assertThat(updatedActor!!.firstName).isEqualTo(request.firstName)
        assertThat(updatedActor.lastName).isEqualTo(newActor.lastName)
    }

    @Test
    @DisplayName("Delete 예제")
    @Transactional
    fun delete() {
        // given
        val newActor: Actor = Actor()
        newActor.firstName = "John"
        newActor.lastName = "Watson"
        val id = actorRepository.saveWithReturningKey(newActor)

        // when
        val resultCode = actorRepository.delete(id)

        // then
        assertThat(resultCode).isEqualTo(1)
        val result = actorRepository.findById(id)
        assertThat(result).isNull()
    }

    @Test
    @DisplayName("Delete 예제 - Record")
    @Transactional
    fun deleteWithRecord() {
        // given
        val newActor: Actor = Actor()
        newActor.firstName = "American"
        newActor.lastName = "Prometheus"
        val id = actorRepository.saveWithReturningKey(newActor)

        // when
        val result = actorRepository.deleteWithRecord(id)

        // then
        assertThat(result).isEqualTo(1)
    }
}