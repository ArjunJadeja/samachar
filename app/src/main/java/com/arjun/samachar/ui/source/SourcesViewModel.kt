package com.arjun.samachar.ui.source

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.samachar.data.model.Source
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.ui.base.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SourcesViewModel(private val repository: MainRepository) : ViewModel() {

    private val _sourceList = MutableStateFlow<UiState<List<Source>>>(UiState.Loading)

    val sourceList: StateFlow<UiState<List<Source>>> = _sourceList

    fun getSources(countryCode: String) {
        viewModelScope.launch {
            repository.getSources(countryCode)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _sourceList.value = UiState.Error(e.message.toString())
                }.collect {
                    _sourceList.value = UiState.Success(it)
                }
        }
    }

}