package com.arjun.samachar.ui.headlines.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.samachar.data.local.entity.BookmarkHeadline
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.ui.base.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _headlineList = MutableStateFlow<UiState<List<BookmarkHeadline>>>(UiState.Loading)

    val headlineList: StateFlow<UiState<List<BookmarkHeadline>>> = _headlineList

    init {
        getBookmarkedHeadlines()
    }

    private fun getBookmarkedHeadlines() {
        viewModelScope.launch {
            repository.getBookmarkedHeadlines()
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _headlineList.value = UiState.Error(e.message.toString())
                }
                .collect {
                    _headlineList.value = UiState.Success(it)
                }
        }
    }

    fun removeFromBookmarkedHeadlines(headline: BookmarkHeadline) {
        viewModelScope.launch { repository.removeFromBookmarkedHeadlines(headline = headline) }
    }

}