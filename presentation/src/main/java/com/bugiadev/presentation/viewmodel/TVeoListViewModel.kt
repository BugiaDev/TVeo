package com.bugiadev.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bugiadev.domain.repository.TVeoRepository
import com.bugiadev.presentation.display.MovieDisplay
import com.bugiadev.presentation.display.toDisplay
import com.bugiadev.presentation.utils.SingleLiveEvent
import com.bugiadev.presentation.utils.prepareForUI
import javax.inject.Inject
import com.bugiadev.presentation.utils.subscribe

class TVeoListViewModel @Inject constructor(
    private val repository: TVeoRepository
) : BaseViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _movies = MutableLiveData<List<MovieDisplay>>()
    val movies: LiveData<List<MovieDisplay>> = _movies

    private val _selectedMovie = SingleLiveEvent<String>()
    val selectedMovie: LiveData<String> = _selectedMovie

    private lateinit var favorites: Set<String>
    private var moviesNumber: Int = 0
    private var current: Int = 0

    fun start(moviesNumber: Int) {
        this.moviesNumber = moviesNumber
        repository.getLimitedMovieList(from = 0, count = getNextPageSize(0))
            .prepareForUI()
            .subscribe(
                disposables = disposables,
                onSuccess = { movies ->
                    _movies.postValue(movies.map {
                        val display = it.toDisplay()
                        display.isFav = favorites.contains(it.id)
                        display
                    })
                },
                onError = ::handleError
            )
    }

    fun downloadMoreMovies() {
        repository.getLimitedMovieList(from = current, count = getNextPageSize(current))
            .prepareForUI()
            .subscribe(
                disposables = disposables,
                onSuccess = { movies ->
                    _movies.postValue(movies.map {
                        val display = it.toDisplay()
                        display.isFav = favorites.contains(it.id)
                        display
                    })
                },
                onError = ::handleError
            )
    }

    fun onMovieSelected(id: String) {
        _selectedMovie.postValue(id)
    }

    fun setFavorites(favs: MutableSet<String>) {
        favorites = favs
    }

    private fun getNextPageSize(from: Int): Int {
        val size = if (moviesNumber - from > 5) 5 else moviesNumber - from
        current += size
        return size
    }
}