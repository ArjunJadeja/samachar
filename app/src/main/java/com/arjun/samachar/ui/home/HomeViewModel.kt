package com.arjun.samachar.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.samachar.data.model.Headline
import com.arjun.samachar.data.repository.MainRepository
import com.arjun.samachar.ui.base.UiState
import com.arjun.samachar.utils.AppConstants.DEFAULT_COUNTRY_CODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MainRepository) : ViewModel() {

    private val _headlineList = MutableStateFlow<UiState<List<Headline>>>(UiState.Loading)

    val headlineList: StateFlow<UiState<List<Headline>>> = _headlineList

    init {
        getHeadlinesByCountry(DEFAULT_COUNTRY_CODE)
    }

    fun getHeadlinesByCountry(countryCode: String) {
        viewModelScope.launch {
            repository.getHeadlinesByCountry(countryCode = countryCode)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    _headlineList.value = UiState.Error(e.toString())
                }.collect {
                    _headlineList.value = UiState.Success(it)
                }
        }
    }

}