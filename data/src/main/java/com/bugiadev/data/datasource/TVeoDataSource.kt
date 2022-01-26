package com.bugiadev.data.datasource

import com.bugiadev.data.entity.MovieEntity
import com.bugiadev.data.entity.TVeoEntity
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TVeoDataSource {
    companion object {
        const val COMPLETE_MOVIE_LIST = "rtv/v1/GetUnifiedList?client=json&statuses=published&definitions=SD;HD;4K&external_category_id=SED_3880&filter_empty_categories=true"
        const val MOVIE_DETAIL = "rtv/v1/GetVideo?client=json"
    }

    @GET(COMPLETE_MOVIE_LIST)
    fun getCompleteMovieList(): Single<TVeoEntity>

    @GET(COMPLETE_MOVIE_LIST)
    fun getLimitedMovieList(
        @Query("from") from: Int,
        @Query("count") count: Int
    ): Single<TVeoEntity>

    @GET(MOVIE_DETAIL)
    fun getMovieDetail(
        @Query("external_id") id: String
    ): Single<MovieEntity>
}