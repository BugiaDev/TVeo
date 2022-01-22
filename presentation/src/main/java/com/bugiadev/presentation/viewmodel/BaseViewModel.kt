package com.bugiadev.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bugiadev.presentation.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
    protected val disposables = CompositeDisposable()

    private val _error = SingleLiveEvent<ErrorState>()
    val error: LiveData<ErrorState> = _error

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    protected fun handleError(error: Throwable) {
        if (error is NetworkException) postError(NoNetworkError)
        else postError(UnexpectedError)
    }

    protected fun postError(errorState: ErrorState) {
        _error.postValue(errorState)
    }
}