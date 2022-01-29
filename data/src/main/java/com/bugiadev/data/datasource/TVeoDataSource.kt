package com.bugiadev.data.datasource

import com.bugiadev.data.entity.TVeoDetailEntity
import com.bugiadev.data.entity.TVeoEntity
import com.bugiadev.data.entity.TVeoRecommendationEntity
import io.reactivex.Single
import okhttp3.HttpUrl
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface TVeoDataSource {
    // BASE_URL = "https://smarttv.orangetv.orange.es/stv/api/";
    companion object {
        const val COMPLETE_MOVIE_LIST = "rtv/v1/GetUnifiedList?client=json&statuses=published&definitions=SD;HD;4K&external_category_id=SED_3880&filter_empty_categories=true"
        const val MOVIE_DETAIL = "rtv/v1/GetVideo?client=json"
        const val RECOMMENDATIONS_LIST = "reco/v1/GetVideoRecommendationList?client=json&type=all&subscription=false&filter_viewed_content=true&max_results=10&blend=ar_od_blend_2424video"
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
    ): Single<TVeoDetailEntity>

    @GET
    fun getRecommendationsList(
        @Url url: String
    ): Single<TVeoRecommendationEntity>
}