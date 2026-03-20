package com.example.guestservicesapp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.guestservicesapp.ui.screens.*
import com.example.guestservicesapp.viewmodel.SharedViewModel

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
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val sharedVm: SharedViewModel = viewModel()

    val tabs = listOf(
        BottomTab(TabRoutes.Home, "Home", Icons.Filled.Home),
        BottomTab(TabRoutes.Services, "Services", Icons.Filled.Build),
        BottomTab(TabRoutes.Food, "Food", Icons.Filled.Restaurant),
        BottomTab(TabRoutes.Profile, "Profile", Icons.Filled.Person),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedContent(
                        targetState = currentRoute,
                        transitionSpec = {
                            (fadeIn(animationSpec = tween(400)) + slideInVertically()).togetherWith(
                                fadeOut(animationSpec = tween(400)) + slideOutVertically()
                            )
                        }, label = ""
                    ) { route ->
                        Text(
                            text = when (route) {
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
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                    }
                },
                navigationIcon = {
                    val isTopLevel = currentRoute in listOf(TabRoutes.Home, TabRoutes.Services, TabRoutes.Food, TabRoutes.Profile)
                    if (!isTopLevel && currentRoute != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                actions = {
                    if (currentRoute != TabRoutes.Home) {
                        IconButton(onClick = {
                            navController.navigate(TabRoutes.Home) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                launchSingleTop = true
                            }
                        }) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Home, 
                                        contentDescription = "Home", 
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        },
        bottomBar = {
            CustomAnimatedDock(
                tabs = tabs,
                currentRoute = currentRoute ?: TabRoutes.Home,
                onTabSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = TabRoutes.Home,
                modifier = Modifier.padding(paddingValues),
                enterTransition = { fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.95f) },
                exitTransition = { fadeOut(animationSpec = tween(500)) + scaleOut(targetScale = 0.95f) },
                popEnterTransition = { fadeIn(animationSpec = tween(500)) + scaleIn(initialScale = 0.95f) },
                popExitTransition = { fadeOut(animationSpec = tween(500)) + scaleOut(targetScale = 0.95f) }
            ) {
                composable(TabRoutes.Home) { HomeScreen(sharedVm, { navController.navigate(TabRoutes.Housekeeping) }, { navController.navigate(TabRoutes.SOS) }, { navController.navigate(TabRoutes.Food) }, { navController.navigate(TabRoutes.Contact) }) }
                composable(TabRoutes.Services) { ServicesScreen({ navController.navigate(TabRoutes.Housekeeping) }, { navController.navigate(TabRoutes.SOS) }, { navController.navigate(TabRoutes.Contact) }, { navController.navigate(TabRoutes.RequestHistory) }) }
                composable(TabRoutes.Food) { FoodScreen() }
                composable(TabRoutes.Profile) { ProfileScreen(onLogout = {}) }
                composable(TabRoutes.Housekeeping) { HousekeepingScreen(sharedVm, { navController.navigate(TabRoutes.RequestHistory) }) }
                composable(TabRoutes.RequestHistory) { RequestHistoryScreen(sharedVm) }
                composable(TabRoutes.SOS) { SOSScreen() }
                composable(TabRoutes.Contact) { ContactHotelScreen() }
            }
        }
    }
}

@Composable
fun CustomAnimatedDock(
    tabs: List<BottomTab>,
    currentRoute: String,
    onTabSelected: (String) -> Unit
) {
    val isDark = isSystemInDarkTheme()
    
    Surface(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(36.dp),
        color = if (isDark) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f) else Color.Black.copy(alpha = 0.92f),
        shadowElevation = 15.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = currentRoute == tab.route
                
                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.25f else 1f,
                    animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
                    label = "scale"
                )

                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White.copy(alpha = 0.15f) else Color.Transparent,
                    animationSpec = tween(350),
                    label = "color"
                )

                Box(
                    modifier = Modifier
                        .height(48.dp)
                        .clip(CircleShape)
                        .background(bgColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabSelected(tab.route) }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = if (isSelected) Color.White else Color.Gray,
                            modifier = Modifier.scale(scale)
                        )
                        AnimatedVisibility(
                            visible = isSelected,
                            enter = expandHorizontally(animationSpec = tween(400)) + fadeIn(),
                            exit = shrinkHorizontally(animationSpec = tween(400)) + fadeOut()
                        ) {
                            Text(
                                text = tab.label,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
