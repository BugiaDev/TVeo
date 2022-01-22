package com.bugiadev.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bugiadev.domain.repository.TVeoRepository
import com.bugiadev.presentation.utils.prepareForUI
import javax.inject.Inject
import com.bugiadev.presentation.utils.subscribe

class SplashViewModel @Inject constructor(
    private val repository: TVeoRepository
) : BaseViewModel() {
    private val _moviesNumber = MutableLiveData<Int>()
    val moviesNumber: LiveData<Int> = _moviesNumber

    fun getMoviesNumber() {
        repository.getCompleteMovieList()
            .prepareForUI()
            .subscribe(
                disposables = disposables,
                onSuccess = { movies ->
                    _moviesNumber.postValue(movies.size)
                },
                onError = ::handleError
            )
    }
}