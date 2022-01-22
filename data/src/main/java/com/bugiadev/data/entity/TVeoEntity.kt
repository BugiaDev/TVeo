package com.bugiadev.data.entity

import com.bugiadev.domain.DomainMappable
import com.bugiadev.domain.models.AttachmentModel
import com.bugiadev.domain.models.MovieModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TVeoEntity(
    @Json(name = "response") val response: List<MovieEntity>
)

@JsonClass(generateAdapter = true)
data class MovieEntity(
    @Json(name = "externalId") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "type") val type: String,
    @Json(name = "year") val year: Int,
    @Json(name = "description") val description: String,
    @Json(name = "attachments") val attachments: List<Attachment>
) : DomainMappable<MovieModel> {
    override fun toDomain(): MovieModel = MovieModel(
        id = id,
        name = name,
        type = type,
        year = year,
        description = description,
        attachments = attachments.map { it.toDomain() }
    )
}

@JsonClass(generateAdapter = true)
data class Attachment(
    @Json(name = "assetId") val assetId: String,
    @Json(name = "name") val name: String,
    @Json(name = "assetName") val assetName: String,
    @Json(name = "value") val value: String
) : DomainMappable<AttachmentModel> {
    override fun toDomain(): AttachmentModel = AttachmentModel(
        assetId = assetId,
        name = name,
        assetName = assetName,
        value = value
    )
}