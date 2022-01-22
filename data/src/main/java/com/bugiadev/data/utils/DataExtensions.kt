package com.bugiadev.data.utils

import com.bugiadev.data.network.NoNetworkException
import io.reactivex.Single
import java.net.SocketTimeoutException

fun Int.Companion.empty() = 0

fun String.Companion.empty() = ""

fun <T> Single<T>.mapNetworkErrors(): Single<T> =
    this.onErrorResumeNext { error ->
        when (error) {
            is SocketTimeoutException -> Single.error(NoNetworkException(error))
            else -> Single.error(error)
        }
    }