package com.bugiadev.presentation.display

import android.annotation.SuppressLint
import com.bugiadev.di.BuildConfig
import com.bugiadev.domain.models.MovieModel
import com.bugiadev.domain.models.RecommendedMovieModel
import com.bugiadev.presentation.utils.PresentationConstants
import com.bugiadev.presentation.utils.PresentationConstants.ATTACHMENT_COVER_NAME
import java.util.concurrent.TimeUnit
import java.lang.String.format as format

data class MovieDisplay(
    val id: String?,
    val assetExternalId: String?,
    val name: String?,
    val description: String?,
    val type: String?,
    val year: Int?,
    val imageURI: String?,
    var isFav: Boolean? = false,
    val duration: String?
)

@SuppressLint("DefaultLocale")
fun MovieModel.toDisplay(): MovieDisplay {
    val selectedAttachment =
        attachments.single { attachment -> attachment.name == ATTACHMENT_COVER_NAME }
    val imageURI = BuildConfig.BASE_URL + BuildConfig.IMAGES_ENDPOINT + selectedAttachment.value
    val filmAdaptedDuration = format(
        "%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
        TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1)
    ) + "h."
    return MovieDisplay(
        id = id,
        assetExternalId = assetExternalId,
        name = name,
        description = description,
        type = type,
        year = year,
        imageURI = imageURI,
        duration = filmAdaptedDuration
    )
}

fun RecommendedMovieModel.toDisplay(): MovieDisplay {
    val selectedAttachment =
        images.single { attachment -> attachment.name == ATTACHMENT_COVER_NAME }
    val imageURI = BuildConfig.BASE_URL + BuildConfig.IMAGES_ENDPOINT + selectedAttachment.value
    return MovieDisplay(
        id = id,
        assetExternalId = id,
        name = name,
        description = "No description available",
        type = type,
        year = Int.MIN_VALUE,
        imageURI = imageURI,
        duration = "Unknown"
    )
}