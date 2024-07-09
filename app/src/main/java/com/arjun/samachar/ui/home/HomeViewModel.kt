package com.arjun.samachar.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.samachar.data.model.Headline
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.ui.base.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MainRepository) : ViewModel() {

    private val _headlineList = MutableStateFlow<UiState<List<Headline>>>(UiState.Loading)

    val headlineList: StateFlow<UiState<List<Headline>>> = _headlineList

    private var fetchJob: Job? = null

    fun clearHeadlineList() {
        _headlineList.update { UiState.Loading }
    }

    private fun launchFetching(block: suspend () -> Unit) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            block()
        }
    }

    fun getHeadlinesByCountry(countryCode: String) {
        launchFetching {
            repository.getHeadlinesByCountry(
                countryCode = countryCode
            ).handleNewsListUpdate()
        }
    }

    fun getHeadlinesBySource(sourceId: String) {
        launchFetching {
            repository.getHeadlinesBySource(
                sourceId = sourceId
            ).handleNewsListUpdate()
        }
    }

    fun getHeadlinesByLanguage(countryCode: String, languageCode: String) {
        launchFetching {
            repository.getHeadlinesByLanguage(
                countryCode = countryCode,
                languageCode = languageCode
            ).handleNewsListUpdate()
        }
    }

    private suspend fun Flow<List<Headline>>.handleNewsListUpdate() {
        this.flowOn(Dispatchers.IO)
            .catch { e ->
                _headlineList.value = UiState.Error(e.toString())
            }.collect {
                _headlineList.value = UiState.Success(it)
            }
    }

}