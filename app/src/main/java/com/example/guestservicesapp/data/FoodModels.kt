package com.yourpackage.guestservices.data

data class MenuItemDto(
    val id: String,
    val name: String,
    val description: String?,
    val price: Double,
    val category: String,
    val isAvailable: Boolean
)

data class BuffetScheduleDto(
    val id: String,
    val title: String,          // e.g., "Breakfast Buffet"
    val timeSlot: String,       // e.g., "7:00 AM - 10:30 AM"
    val details: String?
)

data class FoodResponseDto(
    val buffet: List<BuffetScheduleDto>,
    val menu: List<MenuItemDto>
)