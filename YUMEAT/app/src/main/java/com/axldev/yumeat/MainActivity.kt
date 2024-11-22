package com.axldev.yumeat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.axldev.yumeat.clientViews.UserMainScreenContent
import com.axldev.yumeat.rootViews.ActiveAlertsOffersScreen
import com.axldev.yumeat.rootViews.RegisteredUsersScreen
import com.axldev.yumeat.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

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
                            navController.navigate("login") {
                                popUpTo("onboarding") { inclusive = true }
                            }
                        }
                    )
                }

                // Pantalla de Registro
                composable("register") {
                    RegisterScreen(
                        onRegisterClick = { email, password, username, userType ->
                            authViewModel.registerUser(email, password, username, userType)
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true }
                            }
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
                            authViewModel.loginUser(email, password) { userType ->
                                when (userType) {
                                    "root" -> {
                                        navController.navigate("root_main") { popUpTo("login") { inclusive = true } }
                                    }
                                    "vendedor" -> {
                                        navController.navigate("owner_main") { popUpTo("login") { inclusive = true } }
                                    }
                                    "cliente" -> {
                                        navController.navigate("client_main") { popUpTo("login") { inclusive = true } }
                                    }
                                    else -> {
                                        // Manejo de error si el userType es null o no válido
                                        Log.d("MainActivity", "User type not found or invalid")
                                    }
                                }
                            }
                        },
                        onRegisterClick = {
                            navController.navigate("register")
                        }
                    )
                }

                // Pantalla principal para Root
                composable("root_main") {
                    ActiveAlertsOffersScreen(
                        onDeleteOfferClick = { offerId ->
                            val db = FirebaseFirestore.getInstance()
                            db.collection("offers").document(offerId).delete().addOnSuccessListener {
                                Log.d("ActiveAlertsOffers", "Offer deleted successfully")
                            }.addOnFailureListener {
                                Log.e("ActiveAlertsOffers", "Error deleting offer: ${it.message}")
                            }
                        }
                    )
                }

                // Pantalla principal para Cliente
                composable("client_main") {
                    UserMainScreenContent(
                        onHomeClick = {
                            navController.navigate("owner_main")
                        },
                        onOffersClick = {
                            navController.navigate("business_owner")
                        },
                        onProfileClick = {
                            navController.navigate("user_profile")
                        }
                    )
                }

                // Pantalla principal para Vendedor
                composable("owner_main") {
                    OwnerMainScreenContent(
                        onAddBusinessClick = {
                            navController.navigate("add_business")
                        },
                        onAddOfferClick = {
                            navController.navigate("business_owner")
                        },
                        onLogoutClick = {
                            authViewModel.logOut()
                            navController.navigate("login") {
                                popUpTo("owner_main") { inclusive = true }
                            }
                        },
                        onEditBusinessClick = { businessId ->
                            navController.navigate("edit_business/$businessId")
                        }
                    )
                }

                // Pantalla de Active Alerts & Offers
                composable("active_alerts_offers") {
                    ActiveAlertsOffersScreen(
                        onDeleteOfferClick = { offerId ->
                            val db = FirebaseFirestore.getInstance()
                            db.collection("offers").document(offerId).delete().addOnSuccessListener {
                                Log.d("ActiveAlertsOffers", "Offer deleted successfully")
                            }.addOnFailureListener {
                                Log.e("ActiveAlertsOffers", "Error deleting offer: ${it.message}")
                            }
                        }
                    )
                }

                composable("registered_users") {
                    RegisteredUsersScreen(
                        onEditUserClick = { userId ->
                            navController.navigate("edit_user/$userId") // Navega a la pantalla de edición de usuario
                        },
                        onDeleteUserClick = { userId ->
                            val db = FirebaseFirestore.getInstance()
                            db.collection("users").document(userId).delete().addOnSuccessListener {
                                Log.d("RegisteredUsers", "User deleted successfully")
                            }.addOnFailureListener {
                                Log.e("RegisteredUsers", "Error deleting user: ${it.message}")
                            }
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


