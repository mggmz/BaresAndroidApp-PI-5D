package com.axldev.yumeat

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.axldev.yumeat.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            val currentUser = authViewModel.currentUser.collectAsState().value

            // Inicia con la pantalla de Onboarding
            NavHost(
                navController = navController,
                startDestination = "onboarding"
            ) {
                // Pantalla de Onboarding
                composable("onboarding") {
                    OnboardingScreen(
                        onFinish = {
                            // Navega siempre a la pantalla de registro o login tras el onboarding
                            navController.navigate("login") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    )
                }

                // Pantalla de Registro
                composable("register") {
                    RegisterScreen(
                        onRegisterClick = { email, password, username ->
                            authViewModel.registerUser(email, password, username)
                        },
                        onLoginClick = {
                            navController.navigate("login")
                        }
                    )
                }

                // Pantalla de Login
                composable("login") {
                    LoginScreen(
                        onLoginClick = { email, password ->
                            // Intentar login
                            authViewModel.loginUser(email, password)
                            // Verificar si el login fue exitoso (currentUser no es null)
                            if (authViewModel.currentUser.value != null) {
                                // Redirigir a la pantalla principal del owner
                                navController.navigate("owner_main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        },
                        onRegisterClick = {
                            navController.navigate("register")
                        }
                    )
                }

                // Pantalla Principal del Owner
                composable("owner_main") {
                    OwnerMainScreenContent(
                        onAddBusinessClick = {
                            navController.navigate("add_business")
                        }
                    )
                }

                // Pantalla para Agregar Negocios
                composable("add_business") {
                    AddBusinessScreen(
                        onBusinessAdded = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }

        // Oculta la barra de navegación y la barra de estado
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}
