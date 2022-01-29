package com.bugiadev.data.repository

import com.bugiadev.data.datasource.TVeoDataSource
import com.bugiadev.data.utils.mapNetworkErrors
import com.bugiadev.domain.models.MovieModel
import com.bugiadev.domain.models.RecommendedMovieModel
import com.bugiadev.domain.repository.TVeoRepository
import io.reactivex.Single
import okhttp3.HttpUrl
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
        dataSource.getMovieDetail(id).mapNetworkErrors().map { tVeoDetailEntity ->
            tVeoDetailEntity.response.toDomain()
        }

    override fun getRecommendationsList(id: String): Single<List<RecommendedMovieModel>> {
        val url = HttpUrl.parse("https://smarttv.orangetv.orange.es/stv/api/reco/v1/GetVideoRecommendationList?client=json&type=all&subscription=false&filter_viewed_content=true&max_results=10&blend=ar_od_blend_2424video&params=external_content_id:$id&max_pr_level=8&quality=SD,HD&services=2424VIDEO")
        url?.let {
            return dataSource.getRecommendationsList(it.toString()).mapNetworkErrors().map { tVeoRecommendationEntity ->
                tVeoRecommendationEntity.response.map {
                    it.toDomain()
                }
            }
        } ?: run {
            return Single.just(emptyList())
        }
    }
}