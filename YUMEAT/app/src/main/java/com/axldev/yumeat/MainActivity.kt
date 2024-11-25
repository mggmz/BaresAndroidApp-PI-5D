package com.axldev.yumeat

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                                        navController.navigate("root_main") {
                                            popUpTo("login") {
                                                inclusive = true
                                            }
                                        }
                                    }

                                    "vendedor" -> {
                                        navController.navigate("owner_main") {
                                            popUpTo("login") {
                                                inclusive = true
                                            }
                                        }
                                    }

                                    "cliente" -> {
                                        navController.navigate("client_main") {
                                            popUpTo("login") {
                                                inclusive = true
                                            }
                                        }
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
                    RegisteredPlacesScreen(
                        onLogoutClick = {
                            authViewModel.logOut()
                            navController.navigate("login") {
                                popUpTo("root_main") { inclusive = true }
                            }
                        },
                        onHomeClick = {
                            navController.navigate("root_main") {
                                popUpTo("root_main") { inclusive = true }
                            }
                        },
                        onOffersClick = {
                            navController.navigate("offers")
                        },
                        onProfileClick = {
                            navController.navigate("registered_users")
                        }
                    )
                }

                // Pantalla principal para Cliente
                composable("client_main") {
                    UserMainScreenContent(
                        onHomeClick = {
                            navController.navigate("client_main") {
                                popUpTo("client_main") { inclusive = true }
                            }
                        },
                        onOffersClick = {
                            navController.navigate("client_offers")
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

                //Add Business
                composable("add_business") {
                    AddBusinessScreen(
                        onBusinessAdded = {
                            // Regresa a OwnerMainScreen después de añadir el negocio
                            navController.navigate("business_owner") {
                                popUpTo("business_owner") { inclusive = true }
                            }
                        },
                        onNavigateToHome = {
                            navController.navigate("owner_main") {
                                popUpTo("owner_main") { inclusive = true }
                            }
                        },
                        onNavigateToOffers = {
                            navController.navigate("business_owner") {
                                popUpTo("business_owner") { inclusive = true }
                            }
                        }
                    )
                }

                // Edit Business
                composable(
                    route = "edit_business/{businessId}",
                    arguments = listOf(navArgument("businessId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val businessId = backStackEntry.arguments?.getString("businessId") ?: ""
                    EditBusinessScreen(
                        businessId = businessId,
                        onBusinessUpdated = {
                            navController.popBackStack() // Vuelve a la pantalla anterior después de actualizar
                        },
                        onBusinessDeleted = {
                            navController.popBackStack() // Vuelve a la pantalla anterior después de eliminar
                        },
                        onNavigateToHome = {
                            navController.navigate("owner_main") {
                                popUpTo("owner_main") { inclusive = true }
                            }
                        },
                        onNavigateToOffers = {
                            navController.navigate("business_owner") {
                                popUpTo("business_owner") { inclusive = true }
                            }
                        }
                    )
                }

                composable("business_owner") {
                    BusinessOwnerScreen(
                        onAddEventClick = {
                            navController.navigate("add_event")
                        },
                        onAddOfferClick = {
                            navController.navigate("add_offer")
                        },
                        onEditEventClick = { eventId ->
                            navController.navigate("edit_event/$eventId")
                        },
                        onEditOfferClick = { offerId ->
                            navController.navigate("edit_offer/$offerId")
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
                        },
                        onNavigateToOffers = {
                            if (navController.currentDestination?.route != "business_owner") {
                                navController.navigate("business_owner") {
                                    popUpTo("business_owner") { inclusive = true }
                                }
                            }
                        }
                    )
                }

                // Edit Event
                composable("edit_event/{eventId}") { backStackEntry ->
                    val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable
                    EditEventScreen(
                        eventId = eventId,
                        onEventUpdated = {
                            navController.navigate("business_owner") {
                                popUpTo("business_owner") { inclusive = true }
                            }
                        },
                        onEventDeleted = {
                            navController.navigate("business_owner") {
                                popUpTo("business_owner") { inclusive = true }
                            }
                        },
                        onNavigateToHome = {
                            navController.navigate("owner_main") {
                                popUpTo("owner_main") { inclusive = true }
                            }
                        },
                        onNavigateToOffers = {
                            navController.navigate("business_owner") { // Redirige a BusinessOwnerScreen
                                popUpTo("business_owner") { inclusive = true }
                            }
                        }
                    )
                }

                // Edit Offer
                composable("edit_offer/{offerId}") { backStackEntry ->
                    val offerId = backStackEntry.arguments?.getString("offerId") ?: return@composable
                    EditOfferScreen(
                        offerId = offerId,
                        onOfferUpdated = {
                            navController.navigate("business_owner") {
                                popUpTo("business_owner") { inclusive = true }
                            }
                        },
                        onOfferDeleted = {
                            navController.navigate("business_owner") {
                                popUpTo("business_owner") { inclusive = true }
                            }
                        },
                        onNavigateToHome = {
                            navController.navigate("owner_main") {
                                popUpTo("owner_main") { inclusive = true }
                            }
                        },
                        onNavigateToOffers = {
                            navController.navigate("business_owner") { // Redirige a BusinessOwnerScreen
                                popUpTo("business_owner") { inclusive = true }
                            }
                        }
                    )
                }

                composable("add_event") {
                    AddEventScreen(
                        onEventAdded = {
                            navController.navigate("business_owner") { // Redirige a BusinessOwnerScreen después de añadir un evento
                                popUpTo("business_owner") { inclusive = true }
                            }
                        },
                        onNavigateToHome = {
                            navController.navigate("owner_main") {
                                popUpTo("owner_main") { inclusive = true }
                            }
                        },
                        onNavigateToOffers = {
                            navController.navigate("business_owner") { // Redirige a BusinessOwnerScreen
                                popUpTo("business_owner") { inclusive = true }
                            }
                        }
                    )
                }

                composable("add_offer") {
                    AddOfferScreen(
                        onOfferAdded = {
                            navController.popBackStack() // Regresa a "business_owner" después de agregar una oferta
                        },
                        onNavigateToHome = {
                            navController.navigate("owner_main") // Navega al Home del dueño
                        },
                        onNavigateToOffers = {
                            navController.navigate("business_owner") // Navega a la pantalla de Ofertas
                        }
                    )
                }


                composable("offers") {
                    ActiveAlertsOffersScreen(
                        onLogoutClick = {
                            authViewModel.logOut()
                            navController.navigate("login") {
                                popUpTo("offers") { inclusive = true }
                            }
                        },
                        onHomeClick = {
                            navController.navigate("root_main") {
                                popUpTo("offers") { inclusive = true }
                            }
                        },
                        onOffersClick = {
                            navController.navigate("offers") {
                                popUpTo("offers") { inclusive = true }
                            }
                        },
                        onProfileClick = {
                            navController.navigate("registered_users") {
                                popUpTo("offers") { inclusive = true }
                            }
                        },
                        onDeleteOfferClick = { offerId ->
                            // La lógica de eliminación ya está manejada dentro de ActiveAlertsOffersScreen
                            // Por lo tanto, no es necesario repetirla aquí.
                            // Si deseas manejar algo adicional, puedes hacerlo aquí.
                            val db = FirebaseFirestore.getInstance()
                            db.collection("offers").document(offerId).delete()
                                .addOnSuccessListener {
                                    Log.d("ActiveAlertsOffers", "Offer deleted successfully")
                                }
                                .addOnFailureListener {
                                    Log.e(
                                        "ActiveAlertsOffers",
                                        "Error deleting offer: ${it.message}"
                                    )
                                }
                        },
                        onDeleteEventClick = { eventId ->
                            // Similarmente, manejar la eliminación de eventos si es necesario
                            val db = FirebaseFirestore.getInstance()
                            db.collection("events").document(eventId).delete()
                                .addOnSuccessListener {
                                    Log.d("ActiveAlertsOffers", "Event deleted successfully")
                                }
                                .addOnFailureListener {
                                    Log.e(
                                        "ActiveAlertsOffers",
                                        "Error deleting event: ${it.message}"
                                    )
                                }
                        }
                    )
                }


                // Pantalla principal para Registered Users
                composable("registered_users") {
                    RegisteredUsersScreen(
                        onEditUserClick = { userId ->
                            navController.navigate("edit_user/$userId")
                        },
                        onDeleteUserClick = { userId ->
                            // Lógica de eliminación de usuario aquí si se necesita, o se deja al componente.
                        },
                        onLogoutClick = {
                            authViewModel.logOut()
                            navController.navigate("login") {
                                popUpTo("registered_users") { inclusive = true }
                            }
                        },
                        onHomeClick = {
                            navController.navigate("root_main") {
                                popUpTo("registered_users") { inclusive = true }
                            }
                        },
                        onOffersClick = {
                            navController.navigate("offers") {
                                popUpTo("registered_users") { inclusive = true }
                            }
                        },
                        onProfileClick = {
                            navController.navigate("profile") {
                                popUpTo("registered_users") { inclusive = true }
                            }
                        }
                    )
                }

                // Oculta la barra de navegación y la barra de estado
                window.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        )
            }
        }
    }
}
