package com.bugiadev.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bugiadev.domain.repository.TVeoRepository
import com.bugiadev.presentation.display.MovieDisplay
import com.bugiadev.presentation.display.toDisplay
import com.bugiadev.presentation.utils.PresentationConstants
import com.bugiadev.presentation.utils.PresentationConstants.EXTERNAL_ID_TRAILING
import com.bugiadev.presentation.utils.SingleLiveEvent
import com.bugiadev.presentation.utils.prepareForUI
import com.bugiadev.presentation.utils.subscribe
import javax.inject.Inject

class TVeoRecommendationsViewModel @Inject constructor(
    private val repository: TVeoRepository
) : BaseViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _movies = MutableLiveData<List<MovieDisplay>>()
    val movies: LiveData<List<MovieDisplay>> = _movies

    private val _selectedMovie = SingleLiveEvent<String>()
    val selectedMovie: LiveData<String> = _selectedMovie

    fun getMovieRecommendationsList(movieId: String) {
        repository.getRecommendationsList(movieId)
            .prepareForUI()
            .subscribe(
                disposables = disposables,
                onSuccess = { movies ->
                    _movies.postValue(movies.map {
                        it.toDisplay()
                    })
                },
                onError = ::handleError
            )
    }

    fun onMovieSelected(id: String) {
        _selectedMovie.postValue(id + EXTERNAL_ID_TRAILING)
    }
}