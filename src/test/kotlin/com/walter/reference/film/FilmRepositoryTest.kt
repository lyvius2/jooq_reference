package com.walter.reference.film

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FilmRepositoryTest(
    @Autowired val filmRepository: FilmRepository
) {

    @Test
    @DisplayName("1) 영화정보 조회")
    fun findByIdTest() {
        // given & when
        val film = filmRepository.findById(1L)

        // then
        assertThat(film).isNotNull
    }
}