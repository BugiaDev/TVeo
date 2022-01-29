package com.bugiadev.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bugiadev.domain.models.AttachmentModel
import com.bugiadev.domain.models.MovieModel
import com.bugiadev.domain.repository.TVeoRepository
import com.bugiadev.presentation.lifecycle.observeOnce
import com.bugiadev.presentation.rules.RxSchedulerRule
import com.bugiadev.presentation.viewmodel.TVeoListViewModel
import com.bugiadev.presentation.viewmodel.UnexpectedError
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.subjects.SingleSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TVeoListViewModelTest {
    @get:Rule
    val schedulerRule = RxSchedulerRule()

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private val repository: TVeoRepository = mockk()
    private lateinit var viewModel: TVeoListViewModel
    var acceptedMoviesNumber: Int = 5

    @Before
    fun setUp() {
        viewModel = TVeoListViewModel(repository)
    }

    @Test
    fun `limited movie list is retrieved successfully and return right number of movies`() {
        // Given
        val subject = SingleSubject.create<List<MovieModel>>()
        val model = mockMovieList(acceptedMoviesNumber)
        every { repository.getLimitedMovieList(0, acceptedMoviesNumber) } returns subject

        // When
        viewModel.start(acceptedMoviesNumber)

        // Then
        verify(exactly = 1) { repository.getLimitedMovieList(0, acceptedMoviesNumber) }
        viewModel.loading.observeOnce { assert(it) }
        subject.onSuccess(model)
        viewModel.loading.observeOnce { assert(!it) }
        viewModel.movies.observeOnce {
            assert(it.size == acceptedMoviesNumber)
        }
    }

    @Test
    fun `limited movie list is not retrieved successfully due to an error`() {
        // Given
        val subject = SingleSubject.create<List<MovieModel>>()
        every { repository.getLimitedMovieList(0, acceptedMoviesNumber) } returns subject

        // When
        viewModel.start(acceptedMoviesNumber)

        // Then
        verify(exactly = 1) { repository.getLimitedMovieList(0, acceptedMoviesNumber) }
        viewModel.loading.observeOnce { assert(it) }
        subject.onError(Throwable())
        viewModel.loading.observeOnce { assert(!it) }
        viewModel.error.observeOnce { assert(it is UnexpectedError) }
    }

    @Test
    fun `limited movie list is retrieved successfully when we need more movies in the view`() {
        // Given
        val subject = SingleSubject.create<List<MovieModel>>()
        val model = mockMovieList(acceptedMoviesNumber)
        every { repository.getLimitedMovieList(acceptedMoviesNumber, acceptedMoviesNumber) } returns subject

        // When
        viewModel.downloadMoreMovies()

        // Then
        verify(exactly = 1) { repository.getLimitedMovieList(acceptedMoviesNumber, acceptedMoviesNumber) }
        subject.onSuccess(model)
        viewModel.movies.observeOnce {
            assert(it.size == acceptedMoviesNumber)
        }
    }

    @Test
    fun `limited movie list is not retrieved due to an error when we need more movies in the view`() {
        // Given
        val subject = SingleSubject.create<List<MovieModel>>()
        every { repository.getLimitedMovieList(acceptedMoviesNumber, acceptedMoviesNumber) } returns subject

        // When
        viewModel.downloadMoreMovies()

        // Then
        verify(exactly = 1) { repository.getLimitedMovieList(acceptedMoviesNumber, acceptedMoviesNumber) }
        subject.onError(Throwable())
        viewModel.error.observeOnce { assert(it is UnexpectedError) }
    }

    @Test
    fun `just checking selected movie in a test to improve coverage`() {
        // When
        val id = "MFO_0000474417"
        viewModel.onMovieSelected(id)

        // Then
        viewModel.selectedMovie.observeOnce {
            assert(it == id)
        }
    }

    //region MOCKS
    private fun mockMovieList(listSize: Int): List<MovieModel> {
        val list = mutableListOf<MovieModel>()
        for (num in 0..listSize) {
            list.add(mockMovie())
        }

        return list
    }

    private fun mockMovie() = MovieModel(
        id = "MFO_0000474417_PAGE_HD",
        assetExternalId = "MFO_0000474417",
        name = "Jurassic Park",
        type = "Movie",
        year = 1992,
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        duration = 5185000,
        attachments = listOf(mockAttachment())
    )

    private fun mockAttachment() = AttachmentModel(
        assetId = "assetId",
        assetName = "assetName",
        name = "COVER4_1",
        value = "/vod/COVER4_1/MFO_0000474417_COVER4_1.jpg"
    )
    //endregion
}