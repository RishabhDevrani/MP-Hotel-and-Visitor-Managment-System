package com.example.guestservicesapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

data class ServiceRequest(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val detail: String,
    val time: String,
    val estimatedTime: String,
    val status: String
)

class SharedViewModel : ViewModel() {
    private val _requests = mutableStateListOf<ServiceRequest>()
    val requests: List<ServiceRequest> = _requests

    fun addRequest(title: String, detail: String) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val currentTime = sdf.format(Date())
        
        // Hardcoded random time between 5 and 30 minutes
        val randomMinutes = (5..30).random()
        val estimated = "$randomMinutes mins"

        _requests.add(0, ServiceRequest(
            title = title,
            detail = detail,
            time = currentTime,
            estimatedTime = estimated,
            status = "Pending"
        ))
    }
}
