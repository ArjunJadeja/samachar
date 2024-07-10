package com.arjun.samachar.ui.headlines.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.samachar.data.model.Headline
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.ui.base.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MainRepository) : ViewModel() {

    private val _searchedHeadlines = MutableStateFlow<UiState<List<Headline>>>(UiState.Loading)

    val searchedHeadlines: StateFlow<UiState<List<Headline>>> = _searchedHeadlines

    private val _queryText = MutableStateFlow("")

    val queryText: StateFlow<String> = _queryText

    fun search(query: String) {

        updateQueryText(query = query)

        clearHeadlines()

        viewModelScope.launch {
            repository.search(query)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _searchedHeadlines.value = UiState.Error(e.message.toString())
                }
                .collect { articles ->
                    _searchedHeadlines.value = UiState.Success(articles)
                }
        }
    }

    private fun updateQueryText(query: String) {
        _queryText.update { query }
    }

    fun clearHeadlines() {
        _searchedHeadlines.update { UiState.Loading }
    }

}