package com.bugiadev.data.network

import java.lang.RuntimeException

sealed class NetworkException(error: Throwable) : RuntimeException(error)

class NoNetworkException(error: Throwable) : NetworkException(error)