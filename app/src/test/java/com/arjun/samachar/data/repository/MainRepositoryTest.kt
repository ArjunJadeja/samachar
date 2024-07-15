package com.arjun.samachar.data.repository

import app.cash.turbine.test
import com.arjun.samachar.data.local.DatabaseService
import com.arjun.samachar.data.remote.NetworkService
import com.arjun.samachar.data.remote.model.Headline
import com.arjun.samachar.data.remote.model.Source
import com.arjun.samachar.utils.DispatcherProvider
import com.arjun.samachar.utils.TestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainRepositoryTest {

    @Mock
    private lateinit var networkService: NetworkService

    @Mock
    private lateinit var databaseService: DatabaseService

    private lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var mainRepository: MainRepository

    @Before
    fun setup() {
        dispatcherProvider = TestDispatcherProvider()
        mainRepository = MainRepository(
            networkService = networkService,
            databaseService = databaseService,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun getTopHeadlines_whenNetworkServiceResponseSuccess_shouldReturnSuccess() {

        runTest {

            val headline = Headline(
                title = "title",
                description = "description",
                url = "url",
                imageUrl = "urlToImage",
                source = Source(sourceId = "sourceId", sourceName = "sourceName")
            )

            val headlines = mutableListOf<Headline>()

            headlines.add(headline)

            doReturn(flowOf(headlines)).`when`(databaseService).getCachedHeadlines()

            runCatching {
                mainRepository.getCachedHeadlines().test {
                    assertEquals(flowOf(headlines), awaitItem())
                    cancelAndIgnoreRemainingEvents()
                }
            }

            verify(databaseService, times(1)).getCachedHeadlines()
        }
    }

    @Test
    fun getCachedHeadlines_whenNetworkServiceResponseError_shouldReturnError() {

        runTest {

            val errorMessage = "Error Message For You"

            doThrow(RuntimeException(errorMessage)).`when`(databaseService).getCachedHeadlines()

            runCatching {
                mainRepository.getCachedHeadlines().test {
                    assertEquals(errorMessage, awaitError().message)
                    cancelAndIgnoreRemainingEvents()
                }
            }

            verify(databaseService, times(1)).getCachedHeadlines()
        }
    }

}