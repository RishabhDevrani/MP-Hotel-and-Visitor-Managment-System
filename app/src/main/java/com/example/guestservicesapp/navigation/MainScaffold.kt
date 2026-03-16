package com.example.guestservicesapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.guestservicesapp.ui.screens.*

private object TabRoutes {
    const val Home = "tab_home"
    const val Services = "tab_services"
    const val Food = "tab_food"
    const val Profile = "tab_profile"

    // Sub routes
    const val Housekeeping = "housekeeping"
    const val SOS = "sos"
    const val Contact = "contact"
    const val RequestHistory = "request_history"
}

data class BottomTab(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val tabs = listOf(
        BottomTab(TabRoutes.Home, "Home") { Icon(Icons.Filled.Home, contentDescription = "Home") },
        BottomTab(TabRoutes.Services, "Services") { Icon(Icons.Filled.Build, contentDescription = "Services") },
        BottomTab(TabRoutes.Food, "Food") { Icon(Icons.Filled.List, contentDescription = "Food") },
        BottomTab(TabRoutes.Profile, "Profile") { Icon(Icons.Filled.Person, contentDescription = "Profile") },
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
                            TabRoutes.Home -> "Guest Services"
                            TabRoutes.Services -> "Services"
                            TabRoutes.Food -> "Food Menu"
                            TabRoutes.Profile -> "My Profile"
                            TabRoutes.Housekeeping -> "Housekeeping"
                            TabRoutes.SOS -> "Emergency SOS"
                            TabRoutes.Contact -> "Contact Hotel"
                            TabRoutes.RequestHistory -> "Request History"
                            else -> "Guest Services"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    if (currentRoute != TabRoutes.Home) {
                        IconButton(onClick = {
                            navController.navigate(TabRoutes.Home) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }) {
                            Icon(Icons.Filled.Home, contentDescription = "Go to Home")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = tab.icon,
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = TabRoutes.Home,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(TabRoutes.Home) {
                HomeScreen(
                    onGoHousekeeping = { navController.navigate(TabRoutes.Housekeeping) },
                    onGoSOS = { navController.navigate(TabRoutes.SOS) },
                    onGoFood = { navController.navigate(TabRoutes.Food) },
                    onGoContact = { navController.navigate(TabRoutes.Contact) }
                )
            }

            composable(TabRoutes.Services) {
                ServicesScreen(
                    onGoHousekeeping = { navController.navigate(TabRoutes.Housekeeping) },
                    onGoSOS = { navController.navigate(TabRoutes.SOS) },
                    onGoContact = { navController.navigate(TabRoutes.Contact) },
                    onGoRequestHistory = { navController.navigate(TabRoutes.RequestHistory) }
                )
            }

            composable(TabRoutes.Food) { FoodScreen() }
            composable(TabRoutes.Profile) { ProfileScreen() }

            // Sub screens
            composable(TabRoutes.Housekeeping) {
                HousekeepingScreen(
                    onOpenHistory = { navController.navigate(TabRoutes.RequestHistory) }
                )
            }
            composable(TabRoutes.RequestHistory) { RequestHistoryScreen() }
            composable(TabRoutes.SOS) { SOSScreen() }
            composable(TabRoutes.Contact) { ContactHotelScreen() }
        }
    }
}
