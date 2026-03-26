package com.example.swfilmfinder.data.remote

import com.example.swfilmfinder.data.remote.dto.FilmDto
import com.example.swfilmfinder.data.remote.dto.FilmListResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SwapiService {
    @GET("films/")
    suspend fun getFilms(@Query("search") search: String? = null): FilmListResponseDto

    @GET("films/{id}/")
    suspend fun getFilmDetails(@Path("id") id: Int): FilmDto
}
