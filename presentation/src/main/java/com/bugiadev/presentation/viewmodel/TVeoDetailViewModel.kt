package com.bugiadev.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bugiadev.domain.repository.TVeoRepository
import com.bugiadev.presentation.display.MovieDisplay
import com.bugiadev.presentation.display.toDisplay
import com.bugiadev.presentation.utils.prepareForUI
import com.bugiadev.presentation.utils.subscribe
import javax.inject.Inject

class TVeoDetailViewModel @Inject constructor(
    private val repository: TVeoRepository
) : BaseViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _movieDetail = MutableLiveData<MovieDisplay>()
    val movieDetail: LiveData<MovieDisplay> = _movieDetail

    fun getMovieDetail(id: String) {
        repository.getMovieDetail(id = id)
            .doOnSubscribe { _loading.postValue(true) }
            .doFinally { _loading.postValue(false) }
            .prepareForUI()
            .subscribe(
                disposables = disposables,
                onSuccess = { movieDetail ->
                    _movieDetail.postValue(movieDetail.toDisplay())
                },
                onError = ::handleError
            )
    }
}