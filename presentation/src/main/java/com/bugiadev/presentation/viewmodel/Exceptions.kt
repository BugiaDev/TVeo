package com.bugiadev.presentation.viewmodel

import java.lang.RuntimeException

sealed class NetworkException(error: Throwable) : RuntimeException(error)

class NoNetworkException(error: Throwable) : NetworkException(error)