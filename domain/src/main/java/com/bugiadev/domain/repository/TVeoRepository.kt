package com.bugiadev.domain.repository

import com.bugiadev.domain.models.MovieModel
import io.reactivex.Single

interface TVeoRepository {
    fun getCompleteMovieList() : Single<List<MovieModel>>
    fun getLimitedMovieList(from: Int, count: Int) : Single<List<MovieModel>>
    fun getMovieDetail(id: String) : Single<MovieModel>
}