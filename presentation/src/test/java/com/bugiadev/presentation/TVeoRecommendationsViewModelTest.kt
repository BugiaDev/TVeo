package com.bugiadev.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bugiadev.domain.models.AttachmentModel
import com.bugiadev.domain.models.RecommendedMovieModel
import com.bugiadev.domain.repository.TVeoRepository
import com.bugiadev.presentation.lifecycle.observeOnce
import com.bugiadev.presentation.rules.RxSchedulerRule
import com.bugiadev.presentation.viewmodel.TVeoRecommendationsViewModel
import com.bugiadev.presentation.viewmodel.UnexpectedError
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.subjects.SingleSubject
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TVeoRecommendationsViewModelTest {
    @get:Rule
    val schedulerRule = RxSchedulerRule()

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private val repository: TVeoRepository = mockk()
    private lateinit var viewModel: TVeoRecommendationsViewModel
    private lateinit var id: String

    @Before
    fun setUp() {
        viewModel = TVeoRecommendationsViewModel(repository)
        id = "MFO_0000474417"
    }

    @Test
    fun `complete movie list is retrieved successfully and its size is returned`() {
        // Given
        val subject = SingleSubject.create<List<RecommendedMovieModel>>()
        val model = mockRecommendationsList()
        every { repository.getRecommendationsList(id) } returns subject

        // When
        viewModel.getMovieRecommendationsList(id)

        // Then
        verify(exactly = 1) { repository.getRecommendationsList(id) }
        viewModel.loading.observeOnce { assert(it) }
        subject.onSuccess(model)
        viewModel.loading.observeOnce { assert(!it) }
        viewModel.movies.observeOnce {
            assert(it[0].id == id)
        }
    }

    @Test
    fun `complete movie list is not retrieved due to an error`() {
        // Given
        val subject = SingleSubject.create<List<RecommendedMovieModel>>()
        every { repository.getRecommendationsList(id) } returns subject

        // When
        viewModel.getMovieRecommendationsList(id)

        // Then
        verify(exactly = 1) { repository.getCompleteMovieList() }
        viewModel.loading.observeOnce { assert(it) }
        subject.onError(Throwable())
        viewModel.loading.observeOnce { assert(!it) }
        viewModel.error.observeOnce { assert(it is UnexpectedError) }
    }

    @Test
    fun `just checking selected movie in a test to improve coverage`() {
        // When
        viewModel.onMovieSelected(id)

        // Then
        viewModel.selectedMovie.observeOnce {
            assert(it == id)
        }
    }

    //region MOCKS
    private fun mockRecommendationsList() = listOf(mockRecommendation())

    private fun mockRecommendation() = RecommendedMovieModel(
        id = "MFO_0000474417_PAGE_HD",
        name = "Jurassic Park",
        type = "Movie",
        images = listOf(mockAttachment())
    )

    private fun mockAttachment() = AttachmentModel(
        assetId = "assetId",
        assetName = "assetName",
        name = "COVER4_1",
        value = "/vod/COVER4_1/MFO_0000474417_COVER4_1.jpg"
    )
    //endregion

}