package com.walter.reference.actor.repository

import com.walter.reference.film.repository.FilmRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqJoinShortCutTest(
    @Autowired val filmRepository: FilmRepository,
) {
    @Test
    @DisplayName("implicitPathJoin_테스트")
    fun implicitPathJoinTest() {
        // given & when
        val original = filmRepository.findFilmWithActorList(1L, 10L)
        val implicit = filmRepository.findFilmWithActorListImplicitPathJoin(1L, 10L)

        // then
        assertThat(original)
            .usingRecursiveFieldByFieldElementComparator()
            .isEqualTo(implicit)
    }

    @Test
    @DisplayName("explicitPathJoin_테스트")
    fun explicitPathJoinTest() {
        // given & when
        val original = filmRepository.findFilmWithActorList(1L, 10L)
        val implicit = filmRepository.findFilmWithActorListExplicitPathJoin(1L, 10L)

        // then
        assertThat(original)
            .usingRecursiveFieldByFieldElementComparator()
            .isEqualTo(implicit)
    }
}