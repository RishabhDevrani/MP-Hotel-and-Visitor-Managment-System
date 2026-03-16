package com.example.guestservicesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("My Stay", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Text("Name: Guest User")
        Text("Room: 304")
        Text("Check-in: 28 Feb")
        Text("Checkout: 02 Mar")
        Spacer(Modifier.height(24.dp))
        OutlinedButton(onClick = { /* logout */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Logout")
        }
    }
}