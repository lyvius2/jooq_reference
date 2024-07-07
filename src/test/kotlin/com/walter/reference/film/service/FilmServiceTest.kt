package com.walter.reference.film.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FilmServiceTest(
    @Autowired val filmService: FilmService,
) {

    @Test
    @DisplayName("영화와 영화에 출연한 배우 정보를 페이징하여 조회한다.")
    fun getFilmActorPageResponseTest() {
        // given
        val page = 2L
        val pageSize = 40L

        // when
        val filmWithActorPages = filmService.getFilmActorPageResponse(page, pageSize)

        // then
        assertThat(filmWithActorPages).isNotNull
        assertThat(filmWithActorPages.filmActorList).hasSizeLessThanOrEqualTo(pageSize.toInt())
        filmWithActorPages.filmActorList.forEach {
            assertThat(it).hasNoNullFieldsOrProperties()
        }
    }
}