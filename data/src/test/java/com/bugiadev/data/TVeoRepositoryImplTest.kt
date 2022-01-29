package com.bugiadev.data

import com.bugiadev.data.datasource.TVeoDataSource
import com.bugiadev.data.entity.*
import com.bugiadev.data.repository.TVeoRepositoryImpl
import com.bugiadev.domain.repository.TVeoRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class TVeoRepositoryImplTest {
    private val dataSource: TVeoDataSource = mockk()
    private lateinit var repository: TVeoRepository

    @Before
    fun setUp() {
        repository = TVeoRepositoryImpl(dataSource)
    }

    @Test
    fun `movie list are retrieved successfully`() {
        // Given
        val data = mockTVeoEntity()
        every { dataSource.getCompleteMovieList() } returns Single.just(data)

        // When
        val sut = repository.getCompleteMovieList().test()

        // Then
        verify(exactly = 1) { dataSource.getCompleteMovieList() }
        sut.assertComplete()
        sut.assertNoErrors()
        sut.assertValue { list ->
            list[0].id == mockMovie().id
        }
    }

    @Test
    fun `limited movie list are retrieved successfully`() {
        // Given
        val data = mockTVeoEntity()
        every { dataSource.getLimitedMovieList(0, 5) } returns Single.just(data)

        // When
        val sut = repository.getLimitedMovieList(0, 5).test()

        // Then
        verify(exactly = 1) { dataSource.getLimitedMovieList(0, 5) }
        sut.assertComplete()
        sut.assertNoErrors()
        sut.assertValue { list ->
            list[0].id == mockMovie().id
        }
    }

    @Test
    fun `specific movie detail retrieved successfully`() {
        // Given
        val data = mockTVeoDetailEntity()
        val movieId = "1234"
        every { dataSource.getMovieDetail(movieId) } returns Single.just(data)

        // When
        val sut = repository.getMovieDetail(movieId).test()

        // Then
        verify(exactly = 1) { dataSource.getMovieDetail(movieId) }
        sut.assertComplete()
        sut.assertNoErrors()
        sut.assertValue { character ->
            character.id == mockMovie().id
        }
    }

    @Test
    fun `recommendations movie list are retrieved successfully`() {
        // Given
        val data = mockTVeoRecommendationEntity()
        val url = "fakeUrl"
        every { dataSource.getRecommendationsList(url) } returns Single.just(data)

        // When
        val sut = repository.getRecommendationsList(url).test()

        // Then
        verify(exactly = 1) { dataSource.getRecommendationsList(url) }
        sut.assertComplete()
        sut.assertNoErrors()
        sut.assertValue { list ->
            list[0].id == mockRecommendation().id
        }
    }

    // region MOCKS
    private fun mockTVeoEntity() = TVeoEntity(
        response = mockMovieList()
    )

    private fun mockTVeoDetailEntity() = TVeoDetailEntity(
        response = mockMovie()
    )

    private fun mockTVeoRecommendationEntity() = TVeoRecommendationEntity(
        response = mockRecommendationList()
    )

    private fun mockMovieList() = listOf(mockMovie())
    private fun mockRecommendationList() = listOf(mockRecommendation())

    private fun mockMovie() = MovieEntity(
        id = "MFO_0000474417_PAGE_HD",
        assetExternalId = "MFO_0000474417",
        name = "Jurassic Park",
        type = "Movie",
        year = 1992,
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        duration = 5185000,
        attachments = listOf(mockAttachment())
    )

    private fun mockRecommendation() = RecommendedMovieEntity(
        id = "MFO_0000474418",
        name = "Jungle Cruise",
        type = "Movie",
        images = listOf(mockAttachment())
    )

    private fun mockAttachment() = Attachment(
        name = "COVER4_1",
        value = "/vod/COVER4_1/MFO_0000474417_COVER4_1.jpg"
    )
    // endregion
}