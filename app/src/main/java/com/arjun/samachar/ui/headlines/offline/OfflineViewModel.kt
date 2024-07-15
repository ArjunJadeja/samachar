package com.arjun.samachar.ui.headlines.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.samachar.data.remote.model.Headline
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.ui.base.UiState
import com.arjun.samachar.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfflineViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _headlineList = MutableStateFlow<UiState<List<Headline>>>(UiState.Loading)

    val headlineList: StateFlow<UiState<List<Headline>>> = _headlineList

    init {
        getCachedHeadlines()
    }

    private fun getCachedHeadlines() {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getCachedHeadlines()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _headlineList.value = UiState.Error(e.toString())
                }.collect {
                    _headlineList.value = UiState.Success(it)
                }
        }
    }

    fun bookmarkHeadline(headline: Headline) {
        viewModelScope.launch { repository.bookmarkHeadline(headline) }
    }

}