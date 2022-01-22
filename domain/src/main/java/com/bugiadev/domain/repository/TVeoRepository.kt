package com.bugiadev.domain.repository

import com.bugiadev.domain.models.MovieModel
import io.reactivex.Single

interface TVeoRepository {
    fun getCompleteMovieList() : Single<List<MovieModel>>
    /*
        fun getMarvelCharactersList(): Single<List<CharacterModel>>
    fun getMarvelCharacterDetail(id: String): Single<CharacterModel>
     */
}