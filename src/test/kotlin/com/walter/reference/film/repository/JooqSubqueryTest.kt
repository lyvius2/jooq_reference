package com.walter.reference.film.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JooqSubqueryTest(
    @Autowired val filmRepository: FilmRepositoryHasA,
) {
    @Test
    @DisplayName("영화별 대여료가 1.0 이하면 'Cheap', 3.0 이하면 'Moderate', 그 이상이면 'Expensive'로 분류하고, 각 영화의 총 재고 수를 조회한다.")
    fun scalaSubqueryTest() {
        // given
        val filmTitle = "EGG"

        // when
        val filmPriceSummaries = filmRepository.findFilmPriceSummaryByFilmTitle(filmTitle)

        // then
        assertThat(filmPriceSummaries).isNotEmpty
        filmPriceSummaries.forEach { assertThat(it.totalInventory).isNotNull() }
    }

    @Test
    @DisplayName("평균 대여 기간이 가장 긴 영화부터 정렬해서 조회한다.")
    fun fromSubqueryTest() {
        // given
        val filmTitle = "EGG"

        // when
        val filmRentalSummaries = filmRepository.findFilmRentalSummaryByFilmTitle(filmTitle)

        // then
        assertThat(filmRentalSummaries).isNotEmpty
        filmRentalSummaries.forEach { assertThat(it.averageRentalDuration).isNotNull() }
    }

    @Test
    @DisplayName("대여된 기록이 있는 영화만 조회")
    fun whereSubqueryTest() {
        // given
        val filmTitle = "EGG"

        // when
        val films = filmRepository.findRentedFilmByTitle(filmTitle)

        // then
        assertThat(films).isNotEmpty
    }
}