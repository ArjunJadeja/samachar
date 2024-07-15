package com.arjun.samachar.ui.headlines.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.arjun.samachar.data.remote.model.Headline
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.utils.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _searchedHeadlines =
        MutableStateFlow<PagingData<Headline>>(value = PagingData.empty())

    val searchedHeadlines: StateFlow<PagingData<Headline>> = _searchedHeadlines

    private val _queryText = MutableStateFlow("")

    val queryText: StateFlow<String> = _queryText

    fun search(query: String) {

        updateQueryText(query = query)

        clearHeadlines()

        viewModelScope.launch(dispatcherProvider.main) {
            repository.search(query)
                .flowOn(dispatcherProvider.io)
                .collect { _searchedHeadlines.value = it }
        }
    }

    private fun updateQueryText(query: String) {
        _queryText.update { query }
    }

    fun clearHeadlines() {
        _searchedHeadlines.update { PagingData.empty() }
    }

    fun bookmarkHeadline(headline: Headline) {
        viewModelScope.launch { repository.bookmarkHeadline(headline = headline) }
    }

}