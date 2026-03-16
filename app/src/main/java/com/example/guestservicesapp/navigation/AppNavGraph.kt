package com.example.guestservicesapp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import com.example.guestservicesapp.ui.screens.LoginScreen
import com.example.guestservicesapp.ui.screens.SplashScreen
import com.google.accompanist.navigation.animation.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph() {

    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "splash",

        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(350)
            ) + fadeIn(animationSpec = tween(350))
        },

        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(350)
            ) + fadeOut(animationSpec = tween(350))
        },

        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(350)
            ) + fadeIn(animationSpec = tween(350))
        },

        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(350)
            ) + fadeOut(animationSpec = tween(350))
        }

    ) {

        composable("splash") {
            SplashScreen(
                onDone = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            MainScaffold()
        }
    }
}