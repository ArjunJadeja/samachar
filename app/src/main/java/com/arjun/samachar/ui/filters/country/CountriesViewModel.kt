package com.arjun.samachar.ui.filters.country

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arjun.samachar.data.model.Country
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
class CountriesViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _countryList = MutableStateFlow<UiState<List<Country>>>(UiState.Loading)

    val countryList: StateFlow<UiState<List<Country>>> = _countryList

    init {
        getAllCountries()
    }

    private fun getAllCountries() {
        viewModelScope.launch(dispatcherProvider.main) {
            repository.getAllCountries()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _countryList.value = UiState.Error(e.message.toString())
                }.collect {
                    _countryList.value = UiState.Success(it)
                }
        }
    }

}