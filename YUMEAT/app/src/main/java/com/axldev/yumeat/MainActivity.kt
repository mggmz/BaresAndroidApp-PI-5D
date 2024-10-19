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
                            authViewModel.loginUser(email, password)
                            if (authViewModel.currentUser.value != null) {
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
                        },
                        onAddOfferClick = {
                            navController.navigate("business_owner")  // Agregado para ir a la pantalla de BusinessOwnerScreen
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

                // Pantalla para editar negocios
                composable("edit_business/{businessId}") { backStackEntry ->
                    val businessId = backStackEntry.arguments?.getString("businessId") ?: return@composable
                    EditBusinessScreen(
                        businessId = businessId,
                        onBusinessUpdated = {
                            navController.popBackStack()
                        },
                        onBusinessDeleted = {
                            navController.popBackStack()
                        },
                        onNavigateToHome = {
                            navController.navigate("owner_main") // Redirige a la pantalla de Owner Main
                        },
                        onNavigateToOffers = {
                            navController.navigate("business_owner") // Redirige a la pantalla de Business Owner
                        }
                    )
                }

                // Pantalla para Agregar Negocios
                composable("add_business") {
                    AddBusinessScreen(
                        onBusinessAdded = {
                            navController.popBackStack()
                        },
                        onNavigateToHome = {
                            navController.navigate("owner_main") // Redirige a la pantalla de Owner Main
                        },
                        onNavigateToOffers = {
                            navController.navigate("business_owner") // Redirige a la pantalla de Business Owner
                        }
                    )
                }

                // Pantalla de BusinessOwnerScreen para ofertas y eventos
                composable("business_owner") {
                    BusinessOwnerScreen(
                        onAddEventClick = {
                            navController.navigate("add_event") // Navegar al formulario de eventos
                        },
                        onAddOfferClick = {
                            navController.navigate("add_offer") // Navegar al formulario de ofertas
                        },
                        onEditEventClick = { eventId ->
                            navController.navigate("edit_event/$eventId")  // Navegar a la pantalla de editar evento
                        },
                        onEditOfferClick = { offerId ->
                            navController.navigate("edit_offer/$offerId")  // Navegar a la pantalla de editar oferta
                        },
                        onLogoutClick = {
                            authViewModel.logOut()
                            navController.navigate("login") {
                                popUpTo("business_owner") { inclusive = true }
                            }
                        },
                        onNavigateToHome = {
                            navController.navigate("owner_main") {
                                popUpTo("business_owner") { inclusive = true }
                            }
                        }
                    )
                }

                // Pantalla para editar un evento
                composable("edit_event/{eventId}") { backStackEntry ->
                    val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
                    if (eventId.isNotEmpty()) {
                        EditEventScreen(
                            eventId = eventId,
                            onEventUpdated = { navController.popBackStack() },
                            onEventDeleted = { navController.popBackStack() },
                            onNavigateToHome = { navController.navigate("owner_main") },
                            onNavigateToOffers = { navController.navigate("business_owner") }
                        )
                    } else {
                        // Maneja el caso en el que eventId esté vacío
                    }
                }

                // Pantalla para editar una oferta
                composable("edit_offer/{offerId}") { backStackEntry ->
                    val offerId = backStackEntry.arguments?.getString("offerId") ?: ""
                    if (offerId.isNotEmpty()) {
                        EditOfferScreen(
                            offerId = offerId,
                            onOfferUpdated = { navController.popBackStack() },
                            onOfferDeleted = { navController.popBackStack() },
                            onNavigateToHome = { navController.navigate("owner_main") },
                            onNavigateToOffers = { navController.navigate("business_owner") }
                        )
                    } else {
                        // Maneja el caso en el que offerId esté vacío
                    }
                }

                // Pantalla para agregar un evento
                composable("add_event") {
                    AddEventScreen(
                        onEventAdded = {
                            navController.popBackStack() // Navega de regreso después de agregar un evento
                        },
                        onNavigateToHome = {
                            navController.navigate("owner_main")
                        },
                        onNavigateToOffers = {
                            navController.navigate("business_owner")
                        }
                    )
                }

// Pantalla para agregar una oferta
                composable("add_offer") {
                    AddOfferScreen(
                        onOfferAdded = {
                            navController.popBackStack() // Navega de regreso después de agregar una oferta
                        },
                        onNavigateToHome = {
                            navController.navigate("owner_main")
                        },
                        onNavigateToOffers = {
                            navController.navigate("business_owner")
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
