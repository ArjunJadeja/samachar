package com.arjun.samachar.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {

    // Network Status
    private val _isNetworkConnected = MutableStateFlow(false)

    val isNetworkConnected: StateFlow<Boolean> = _isNetworkConnected

    fun updateNetworkStatus(isNetworkConnected: Boolean) {
        _isNetworkConnected.update { isNetworkConnected }
    }

}