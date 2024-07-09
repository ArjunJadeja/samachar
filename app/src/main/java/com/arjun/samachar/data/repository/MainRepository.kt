package com.arjun.samachar.data.repository

import com.arjun.samachar.data.api.NetworkService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val networkService: NetworkService) {}