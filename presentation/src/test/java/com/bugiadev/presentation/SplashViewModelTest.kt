package com.bugiadev.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bugiadev.domain.models.AttachmentModel
import com.bugiadev.domain.models.MovieModel
import com.bugiadev.domain.repository.TVeoRepository
import com.bugiadev.presentation.lifecycle.observeOnce
import com.bugiadev.presentation.rules.RxSchedulerRule
import com.bugiadev.presentation.viewmodel.SplashViewModel
import com.bugiadev.presentation.viewmodel.UnexpectedError
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.subjects.SingleSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {
    @get:Rule
    val schedulerRule = RxSchedulerRule()

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private val repository: TVeoRepository = mockk()
    lateinit var viewModel: SplashViewModel

    @Before
    fun setUp() {
        viewModel = SplashViewModel(repository)
    }

    @Test
    fun `complete movie list is retrieved successfully and its size is returned`() {
        // Given
        val subject = SingleSubject.create<List<MovieModel>>()
        val model = mockMovieList()
        every { repository.getCompleteMovieList() } returns subject

        // When
        viewModel.getMoviesNumber()

        // Then
        verify(exactly = 1) { repository.getCompleteMovieList() }
        subject.onSuccess(model)
        viewModel.moviesNumber.observeOnce {
            assert(it == model.size)
        }
    }

    @Test
    fun `complete movie list is not retrieved due to an error`() {
        // Given
        val subject = SingleSubject.create<List<MovieModel>>()
        every { repository.getCompleteMovieList() } returns subject

        // When
        viewModel.getMoviesNumber()

        // Then
        verify(exactly = 1) { repository.getCompleteMovieList() }
        subject.onError(Throwable())
        viewModel.error.observeOnce { assert(it is UnexpectedError) }
    }

    //region MOCKS
    private fun mockMovieList() = listOf(mockMovie())

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