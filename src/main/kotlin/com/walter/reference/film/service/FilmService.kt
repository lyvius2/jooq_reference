package com.walter.reference.film.service

import com.walter.reference.film.repository.FilmRepository
import com.walter.reference.web.FilmWithActorPagedResponse
import com.walter.reference.web.PagedResponse
import org.springframework.stereotype.Service

@Service
class FilmService (
    val filmRepository: FilmRepository
){
    fun getFilmActorPageResponse(page: Long, pageSize: Long): FilmWithActorPagedResponse {
        val filmWithActorList = filmRepository.findFilmWithActorList(page, pageSize)
        val pagedResponse = PagedResponse(page, pageSize)
        return FilmWithActorPagedResponse.from(pagedResponse, filmWithActorList)
    }
}