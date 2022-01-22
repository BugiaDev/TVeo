package com.bugiadev.data.datasource

import com.bugiadev.data.entity.TVeoEntity
import io.reactivex.Single
import retrofit2.http.GET

interface TVeoDataSource {
    companion object {
        const val COMPLETE_MOVIE_LIST = "rtv/v1/GetUnifiedList?client=json&statuses=published&definitions=SD;HD;4K&external_category_id=SED_3880&filter_empty_categories=true"
    }

    @GET(COMPLETE_MOVIE_LIST)
    fun getCompleteMovieList(): Single<TVeoEntity>
    /*
        companion object {
        const val CHARACTERS = "/v1/public/characters"
        const val CHARACTER = "/v1/public/characters/{characterId}"
    }

    @GET(CHARACTERS)
    fun getCharacters(
        @Query("limit") limit: Int? = 10,
        @Query("offset") offset: Int? = 1
    ): Single<MarvelEntity>

    @GET(CHARACTER)
    fun getCharacterDetail(
        @Path("characterId") characterId: String
    ): Single<MarvelEntity>
     */
}