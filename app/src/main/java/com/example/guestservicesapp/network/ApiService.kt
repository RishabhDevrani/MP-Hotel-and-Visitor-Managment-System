package com.yourpackage.guestservices.network

import com.yourpackage.guestservices.data.FoodResponseDto
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("guest/food")
    suspend fun getFood(@Header("Authorization") bearerToken: String): FoodResponseDto
}