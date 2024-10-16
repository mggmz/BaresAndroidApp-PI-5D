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
            val navController: NavHostController = rememberNavController()
            NavHost(navController = navController, startDestination = "onboarding") {
                composable("onboarding") { OnboardingScreen(navController) }
                composable("register") { RegisterScreen() }
            }
        }
    }
}

