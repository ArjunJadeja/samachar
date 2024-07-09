package com.arjun.samachar.ui.search

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
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Headline>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Headline>>> = _uiState

    private val _queryText = MutableStateFlow("")

    val queryText: StateFlow<String> = _queryText

    fun updateQueryText(query: String) {
        _queryText.value = query
    }

    fun search(query: String) {
        viewModelScope.launch {
            repository.search(query)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message.toString())
                }
                .collect { articles ->
                    _uiState.value = UiState.Success(articles)
                }
        }
    }

}