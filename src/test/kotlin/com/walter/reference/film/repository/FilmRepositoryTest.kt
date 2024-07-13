package com.walter.reference.film.repository

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

    @Test
    @DisplayName("2) 모든 정보가 Null이 아니다")
    fun findSimpleFilmInfoByIdTest() {
        // given
        val id = (1..500).random().toLong()

        // when
        val filmSimpleInfo = filmRepository.findSimpleFilmInfoById(id)

        // then
        assertThat(filmSimpleInfo).hasNoNullFieldsOrProperties()
    }

    @Test
    @DisplayName("3) 영화와 영화에 출연한 배우의 정보를 페이징하여 조회")
    fun pagingTest() {
        // given
        val page = 1L
        val pageSize = 10L

        // when
        val fileWithActors = filmRepository.findFilmWithActorList(page, pageSize)

        // then
        assertThat(fileWithActors).hasSizeGreaterThan(0)
    }
}