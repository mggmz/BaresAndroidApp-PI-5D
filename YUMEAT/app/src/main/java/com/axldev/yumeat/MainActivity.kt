package com.axldev.yumeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()  // Create the NavHostController
            NavHost(
                navController = navController,
                startDestination = "onboarding"  // Start at the OnboardingScreen
            ) {
                // Define each screen and pass navController to handle navigation
                composable("onboarding") { OnboardingScreen(navController) }
                composable("register") { RegistroScreen(navController) }
                composable("owner_main") { OwnerMainScreenContent(navController) }
                composable("add_business") { AddBusinessScreen() }  // Define the AddBusinessScreen route
            }
        }
    }
}


