package com.bugiadev.data.repository

import com.bugiadev.data.datasource.TVeoDataSource
import com.bugiadev.data.utils.mapNetworkErrors
import com.bugiadev.domain.models.MovieModel
import com.bugiadev.domain.repository.TVeoRepository
import io.reactivex.Single
import javax.inject.Inject

class TVeoRepositoryImpl @Inject constructor(
    private val dataSource: TVeoDataSource
) : TVeoRepository {
    override fun getCompleteMovieList(): Single<List<MovieModel>> =
        dataSource.getCompleteMovieList().mapNetworkErrors().map { tVeoEntity ->
            tVeoEntity.response.map {
                it.toDomain()
            }
        }
    /*
    class MarvelRepositoryImpl @Inject constructor(
    private val dataSource: MarvelDataSource
) : MarvelRepository {
    override fun getMarvelCharactersList(): Single<List<CharacterModel>> =
        dataSource.getCharacters().mapNetworkErrors().map { marvelEntity ->
            marvelEntity.data.characters?.map {
                it.toDomain()
            }
        }

    override fun getMarvelCharacterDetail(id: String): Single<CharacterModel> =
        dataSource.getCharacterDetail(id).mapNetworkErrors().map { marvelEntity ->
            marvelEntity.data.characters?.let {
                it[0].toDomain()
            }
        }
}
     */
}