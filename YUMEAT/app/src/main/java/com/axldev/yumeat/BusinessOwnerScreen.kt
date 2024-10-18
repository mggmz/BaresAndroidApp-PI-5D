package com.axldev.yumeat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BusinessOwnerScreen(
    onAddEventClick: () -> Unit,
    onAddOfferClick: () -> Unit,
    onLogoutClick: () -> Unit, // Parámetro para redirigir a la pantalla de login después de cerrar sesión
    onNavigateToHome: () -> Unit  // Nuevo parámetro para regresar a la pantalla de OwnerMainScreen
) {
    var selectedTab by remember { mutableStateOf("Events") }
    val auth = FirebaseAuth.getInstance()

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Color(0xFFE0E0E0)),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón de Home
                    IconButton(
                        onClick = { onNavigateToHome() },  // Acción para regresar a OwnerMainScreen
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = Color.Gray
                        )
                    }
                    IconButton(
                        onClick = onAddOfferClick,  // Usar el parámetro aquí para la navegación
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.LocalOffer,
                            contentDescription = "Offers",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Botón de cerrar sesión en la parte superior izquierda
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            auth.signOut()
                            onLogoutClick()  // Redirigir al LoginScreen después de cerrar sesión
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = Color.Gray)
                    }

                    // Imagen del logo en lugar de texto "Yum Eat"
                    Image(
                        painter = painterResource(id = R.drawable.applogo),  // Reemplaza con tu logo
                        contentDescription = "Yum Eat Logo",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(start = 85.dp, top = 30.dp, bottom = 50.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Menú de navegación (Events/Offers)
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp) // Añadir un padding lateral para separación
                ) {
                    Tab(
                        selected = selectedTab == "Events",
                        onClick = { selectedTab = "Events" },
                        modifier = Modifier
                            .background(
                                if (selectedTab == "Events") Color(0xFFFFA500)
                                else Color.Transparent,
                                shape = RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp) // Solo redondear esquinas izquierdas
                            )
                            .padding(vertical = 12.dp, horizontal = 24.dp) // Padding para darle más cuerpo a la píldora
                            .weight(1f),
                    ) {
                        Text(
                            text = "Events",
                            color = if (selectedTab == "Events") Color.White else Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp)) // Añadir un espaciador entre las pestañas
                    Tab(
                        selected = selectedTab == "Offers",
                        onClick = { selectedTab = "Offers" },
                        modifier = Modifier
                            .background(
                                if (selectedTab == "Offers") Color(0xFFFFA500)
                                else Color.Transparent,
                                shape = RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp) // Solo redondear esquinas derechas
                            )
                            .padding(vertical = 12.dp, horizontal = 24.dp) // Padding para darle más cuerpo a la píldora
                            .weight(1f),
                    ) {
                        Text(
                            text = "Offers",
                            color = if (selectedTab == "Offers") Color.White else Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de eventos u ofertas
                if (selectedTab == "Events") {
                    EventList()
                } else {
                    OfferList()
                }
            }

            // FloatingActionButton para agregar eventos u ofertas
            FloatingActionButton(
                onClick = { if (selectedTab == "Events") onAddEventClick() else onAddOfferClick() },
                shape = CircleShape,
                containerColor = Color(0xFF0072A3),
                modifier = Modifier
                    .size(100.dp)  // Aumentar el tamaño en un 10%
                    .align(Alignment.BottomEnd)  // Alinearlo a la parte inferior derecha
                    .padding(16.dp)  // Añadir padding para separarlo de los bordes
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Event/Offer", tint = Color.White)
            }
        }
    }
}

@Composable
fun EventList() {
    Column {
        repeat(2) { index ->
            EventCard(title = "Evento $index", place = "Lugar $index", date = "Fecha $index")
        }
    }
}

@Composable
fun OfferList() {
    Column {
        repeat(2) { index ->
            OfferCard(title = "Oferta $index", place = "Lugar $index", date = "Fecha $index")
        }
    }
}

@Composable
fun EventCard(title: String, place: String, date: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold)
                Text(text = place, color = Color.Gray)
                Text(text = date, color = Color.Gray)
            }
        }
    }
}

@Composable
fun OfferCard(title: String, place: String, date: String) {
    EventCard(title, place, date) // Reutiliza el diseño de EventCard.
}

@Composable
fun BusinessOwnerScreenPreview() {
    BusinessOwnerScreen(
        onAddEventClick = { /* Acción de agregar evento */ },
        onAddOfferClick = { /* Acción de agregar oferta */ },
        onLogoutClick = { /* Acción de cerrar sesión */ },
        onNavigateToHome = { /* Acción para regresar a OwnerMainScreen */ }
    )
}

