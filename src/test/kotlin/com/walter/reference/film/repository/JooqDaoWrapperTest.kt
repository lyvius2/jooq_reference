package com.walter.reference.film.repository

import org.assertj.core.api.Assertions.assertThat
import org.jooq.types.UInteger
import org.jooq.types.UShort
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqDaoWrapperTest(
    @Autowired val filmRepositoryIsA: FilmRepositoryIsA,
    @Autowired val filmRepositoryHasA: FilmRepositoryHasA,
) {
    @Test
    fun testIsA() {
        // given
        val id = UInteger.valueOf(10L)

        // when
        val film = filmRepositoryIsA.findById(id)

        // then
        assertThat(film).isNotNull
    }

    @Test
    fun testHasA() {
        // given
        val from = 100
        val to = 180

        // when
        val films = filmRepositoryHasA.findByRangeBetween(from, to)

        // when
        assertThat(films).allSatisfy {
            assertThat(it.length).isBetween(UShort.valueOf(from), UShort.valueOf(to))
        }
    }
}