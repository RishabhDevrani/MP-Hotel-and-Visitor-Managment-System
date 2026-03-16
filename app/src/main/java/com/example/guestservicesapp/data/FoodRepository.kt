package com.yourpackage.guestservices.data

import com.yourpackage.guestservices.network.RetrofitClient

class FoodRepository {
    suspend fun fetchFood(token: String): FoodResponseDto {
        return RetrofitClient.api.getFood(bearerToken = "Bearer $token")
    }
}