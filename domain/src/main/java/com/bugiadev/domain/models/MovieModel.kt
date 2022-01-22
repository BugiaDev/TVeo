package com.bugiadev.domain.models

data class MovieModel(
    val id: String,
    val name: String,
    val type: String,
    val year: Int,
    val description: String,
    val attachments: List<AttachmentModel>
)

data class AttachmentModel(
    val assetId: String,
    val name: String,
    val assetName: String,
    val value: String
)