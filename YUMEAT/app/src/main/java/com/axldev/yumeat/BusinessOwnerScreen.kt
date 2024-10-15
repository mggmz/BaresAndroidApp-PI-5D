package com.axldev.yumeat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn

@Composable
fun BusinessOwnerScreen() {
    var selectedTab by remember { mutableStateOf("Events") }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                //backgroundColor = Color(0xFFE9E9E9) // Color de fondo de la barra
            ) {
                // Botón de navegación para Home
                IconButton(onClick = { /* Lógica para ir a Home */ }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home") // Ícono para el botón Home
                }
                Spacer(Modifier.weight(1f)) // Espacio flexible para centrar el botón
                // Botón de navegación para Location
                IconButton(onClick = { /* Lógica para ir a Location */ }) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Location") // Ícono para el botón Location
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(Color.White),
                verticalArrangement = Arrangement.SpaceBetween // Espacio entre elementos
            ) {
                // Encabezado
                Text(
                    text = "Yum",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "Eat",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Menú de navegación
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = selectedTab == "Events",
                        onClick = { selectedTab = "Events" },
                        modifier = Modifier
                            .background(if (selectedTab == "Events") Color(0xFFFFA500) else Color.Transparent)
                            .padding(8.dp)
                            .weight(1f),
                    ) {
                        Text(text = "Events", color = if (selectedTab == "Events") Color.White else Color.Black)
                    }
                    Tab(
                        selected = selectedTab == "Offers",
                        onClick = { selectedTab = "Offers" },
                        modifier = Modifier
                            .background(if (selectedTab == "Offers") Color(0xFFFFA500) else Color.Transparent)
                            .padding(8.dp)
                            .weight(1f),
                    ) {
                        Text(text = "Offers", color = if (selectedTab == "Offers") Color.White else Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de eventos u ofertas
                if (selectedTab == "Events") {
                    EventList()
                } else {
                    OfferList()
                }

                // Botón centrado en la parte inferior
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center // Centrar el botón
                ) {
                    FloatingActionButton(
                        onClick = { /* Acción para agregar */ },
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(50),
                        containerColor = Color.White // Color de fondo del botón
                    ) {
                        Text(text = "+", fontSize = 24.sp, color = Color(0xFF000000)) // Texto del botón
                    }
                }
            }
        }
    )
}

@Composable
fun EventList() {
    Column {
        // Aquí puedes agregar las tarjetas de eventos
        repeat(2) { index ->
            EventCard(title = "Evento $index", place = "Lugar $index", date = "Fecha $index")
        }
    }
}

@Composable
fun OfferList() {
    Column {
        // Aquí puedes agregar las tarjetas de ofertas
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
            // Espacio para imagen (placeholder)
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
    // Similar a EventCard pero para ofertas
    EventCard(title, place, date) // Puedes reutilizar el mismo diseño o modificarlo.
}

@Composable
fun BusinessOwnerScreenPreview() {
    BusinessOwnerScreen()
}
