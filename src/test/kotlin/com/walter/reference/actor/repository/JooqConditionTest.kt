package com.walter.reference.actor.repository

import com.walter.reference.actor.dto.ActorFilmography
import org.assertj.core.api.Assertions.assertThat
import org.jooq.generated.tables.pojos.Actor
import com.walter.reference.actor.dto.ActorFilmographySearchCondition
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqConditionTest(
    @Autowired val actorRepository: ActorRepository,
) {

    @Test
    @DisplayName("And 조건 검색")
    fun andConditionTest() {
        // given
        val firstName = "ED"
        val lastName = "CHASE"

        // when
        val actors: List<Actor> = actorRepository.findByFirstAndLastName(firstName, lastName)

        // then
        assertThat(actors).isNotEmpty
    }

    @Test
    @DisplayName("OR 조건 검색")
    fun orConditionTest() {
        // given
        val firstName = "ED"
        val lastName = "CHASE"

        // when
        val actors: List<Actor> = actorRepository.findByFirstOrLastName(firstName, lastName)

        // then
        assertThat(actors).isNotEmpty
    }

    @Test
    @DisplayName("IN 조건")
    fun inConditionTest() {
        // given
        val ids = mutableListOf(1L, 2L, 7L)

        // when
        val actors: List<Actor> = actorRepository.findByActorIdIn(ids)

        // then
        assertThat(actors).isNotEmpty
        assertThat(actors.size).isEqualTo(ids.size)
    }

    @Test
    @DisplayName("IN 조건 (Empty List일 때 제외)")
    fun inConditionEmptyListTest() {
        // given & when
        val allActors: List<Actor> = actorRepository.findByActorIdIn(mutableListOf())

        // then
        assertThat(allActors).hasSizeGreaterThan(1)
    }

    @Test
    @DisplayName("다중 조건 검색 - 배우 이름으로 조회")
    fun searchMultiConditionTest1() {
        // given
        val condition = ActorFilmographySearchCondition("MCQUEEN")

        // when
        val actorFilmographies: List<ActorFilmography> = actorRepository.findFilmography(condition)

        // then
        assertThat(actorFilmographies).isNotEmpty
    }

    @Test
    @DisplayName("다중 조건 검색 - 배우 이름과 영화 제목으로 검색")
    fun searchMultiConditionTest2() {
        // given
        val condition = ActorFilmographySearchCondition("MCQUEEN", "SWEETHEARTS")

        // when
        val actorFilmographies: List<ActorFilmography> = actorRepository.findFilmography(condition)

        // then
        assertThat(actorFilmographies).isNotEmpty
        assertThat(actorFilmographies.size).isEqualTo(1)
    }
}