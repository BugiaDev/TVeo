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

    override fun getLimitedMovieList(from: Int, count: Int): Single<List<MovieModel>> =
        dataSource.getLimitedMovieList(from, count).mapNetworkErrors().map { tVeoEntity ->
            tVeoEntity.response.map {
                it.toDomain()
            }
        }

    override fun getMovieDetail(id: String): Single<MovieModel> =
        dataSource.getMovieDetail(id).mapNetworkErrors().map { movieEntity ->
            movieEntity.toDomain()
        }
}