package com.bugiadev.presentation.display

import com.bugiadev.di.BuildConfig
import com.bugiadev.domain.models.MovieModel
import com.bugiadev.presentation.utils.PresentationConstants
import com.bugiadev.presentation.utils.PresentationConstants.ATTACHMENT_COVER_NAME

data class MovieDisplay(
    val id: String?,
    val name: String?,
    val description: String?,
    val type: String?,
    val year: Int?,
    val imageURI: String?,
    var isFav: Boolean? = false
)

fun MovieModel.toDisplay(): MovieDisplay {
    val selectedAttachment =
        attachments.single { attachment -> attachment.name == ATTACHMENT_COVER_NAME }
    val imageURI = BuildConfig.BASE_URL + BuildConfig.IMAGES_ENDPOINT + selectedAttachment.value
    return MovieDisplay(
        id = id,
        name = name,
        description = description,
        type = type,
        year = year,
        imageURI = imageURI
    )
}