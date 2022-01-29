package com.bugiadev.data.entity

import com.bugiadev.domain.DomainMappable
import com.bugiadev.domain.models.AttachmentModel
import com.bugiadev.domain.models.MovieModel
import com.bugiadev.domain.models.RecommendedMovieModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TVeoEntity(
    @Json(name = "response") val response: List<MovieEntity>
)

@JsonClass(generateAdapter = true)
data class TVeoDetailEntity(
    @Json(name = "response") val response: MovieEntity
)

@JsonClass(generateAdapter = true)
data class TVeoRecommendationEntity(
    @Json(name = "response") val response: List<RecommendedMovieEntity>
)

@JsonClass(generateAdapter = true)
data class MovieEntity(
    @Json(name = "externalId") val id: String,
    @Json(name = "assetExternalId") val assetExternalId: String,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "year") val year: Int,
    @Json(name = "description") val description: String,
    @Json(name = "duration") val duration: Long = 0,
    @Json(name = "attachments") val attachments: List<Attachment>
) : DomainMappable<MovieModel> {
    override fun toDomain(): MovieModel = MovieModel(
        id = id,
        assetExternalId = assetExternalId,
        name = name,
        type = type,
        year = year,
        description = description,
        duration = duration,
        attachments = attachments.map { it.toDomain() }
    )
}

@JsonClass(generateAdapter = true)
data class Attachment(
    @Json(name = "assetId") val assetId: String = "",
    @Json(name = "name") val name: String,
    @Json(name = "assetName") val assetName: String = "",
    @Json(name = "value") val value: String
) : DomainMappable<AttachmentModel> {
    override fun toDomain(): AttachmentModel = AttachmentModel(
        assetId = assetId,
        name = name,
        assetName = assetName,
        value = value
    )
}

@JsonClass(generateAdapter = true)
data class RecommendedMovieEntity(
    @Json(name = "externalContentId") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "contentType") val type: String,
    @Json(name = "images") val images: List<Attachment>
) : DomainMappable<RecommendedMovieModel> {
    override fun toDomain(): RecommendedMovieModel = RecommendedMovieModel(
        id = id,
        name = name,
        type = type,
        images = images.map { it.toDomain() }
    )
}