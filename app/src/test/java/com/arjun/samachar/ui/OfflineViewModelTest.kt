package com.arjun.samachar.ui

import app.cash.turbine.test
import com.arjun.samachar.data.remote.model.Headline
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.ui.base.UiState
import com.arjun.samachar.ui.headlines.offline.OfflineViewModel
import com.arjun.samachar.utils.DispatcherProvider
import com.arjun.samachar.utils.TestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class OfflineViewModelTest {

    @Mock
    private lateinit var mainRepository: MainRepository

    private lateinit var dispatcherProvider: DispatcherProvider

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()
    }

    @Test
    fun getCachedHeadlines_whenRepositoryResponseSuccess_shouldSetSuccessUiState() {
        runTest {
            doReturn(flowOf(emptyList<Headline>()))
                .`when`(mainRepository)
                .getCachedHeadlines()
            val viewModel = OfflineViewModel(mainRepository, dispatcherProvider)
            viewModel.headlineList.test {
                assertEquals(UiState.Success(emptyList<List<Headline>>()), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(mainRepository, times(1)).getCachedHeadlines()
        }
    }

    @Test
    fun getCachedHeadlines_whenRepositoryResponseError_shouldSetErrorUiState() {
        runTest {
            val errorMessage = "Error Message For You"
            doReturn(flow<List<Headline>> {
                throw IllegalStateException(errorMessage)
            }).`when`(mainRepository)
                .getCachedHeadlines()
            val viewModel = OfflineViewModel(mainRepository, dispatcherProvider)
            viewModel.headlineList.test {
                assertEquals(
                    UiState.Error(IllegalStateException(errorMessage).toString()),
                    awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(mainRepository, times(1)).getCachedHeadlines()
        }
    }

}