package com.axldev.yumeat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun BusinessOwnerScreen(
    onAddEventClick: () -> Unit,
    onAddOfferClick: () -> Unit,
    onEditEventClick: (String) -> Unit,  // Nuevo parámetro para editar eventos
    onEditOfferClick: (String) -> Unit,  // Nuevo parámetro para editar ofertas
    onLogoutClick: () -> Unit,
    onNavigateToHome: () -> Unit
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
                    IconButton(
                        onClick = { onNavigateToHome() },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = Color.Gray
                        )
                    }
                    IconButton(
                        onClick = onAddOfferClick,
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            auth.signOut()
                            onLogoutClick()
                        },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = Color.Gray)
                    }

                    Image(
                        painter = painterResource(id = R.drawable.applogo),
                        contentDescription = "Yum Eat Logo",
                        modifier = Modifier
                            .size(200.dp)
                            .padding(start = 85.dp, top = 30.dp, bottom = 50.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Tab(
                        selected = selectedTab == "Events",
                        onClick = { selectedTab = "Events" },
                        modifier = Modifier
                            .background(
                                if (selectedTab == "Events") Color(0xFFFFA500)
                                else Color.Transparent,
                                shape = RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp)
                            )
                            .padding(vertical = 12.dp, horizontal = 24.dp)
                            .weight(1f),
                    ) {
                        Text(
                            text = "Events",
                            color = if (selectedTab == "Events") Color.White else Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Tab(
                        selected = selectedTab == "Offers",
                        onClick = { selectedTab = "Offers" },
                        modifier = Modifier
                            .background(
                                if (selectedTab == "Offers") Color(0xFFFFA500)
                                else Color.Transparent,
                                shape = RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp)
                            )
                            .padding(vertical = 12.dp, horizontal = 24.dp)
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
                    EventList(onEditEventClick)  // Pasa el parámetro para editar eventos
                } else {
                    OfferList(onEditOfferClick)  // Pasa el parámetro para editar ofertas
                }
            }

            FloatingActionButton(
                onClick = { if (selectedTab == "Events") onAddEventClick() else onAddOfferClick() },
                shape = CircleShape,
                containerColor = Color(0xFF0072A3),
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Event/Offer", tint = Color.White)
            }
        }
    }
}

@Composable
fun EventList(onEditEventClick: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val events = remember { mutableStateOf(listOf<Map<String, Any>>()) }
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(currentUser?.uid) {
        if (currentUser != null) {
            try {
                val eventDocs = db.collection("events")
                    .whereEqualTo("userUID", currentUser.uid)
                    .get()
                    .await()
                // Mapear los datos incluyendo el ID del documento
                events.value = eventDocs.documents.map { doc ->
                    doc.data?.toMutableMap()?.also { it["eventId"] = doc.id } ?: emptyMap()
                }
            } catch (e: Exception) {
                // Manejar el error si ocurre
            }
        }
    }

    // Mostrar los eventos en la interfaz
    Column {
        if (events.value.isEmpty()) {
            Text("No events found", modifier = Modifier.padding(16.dp))
        } else {
            events.value.forEach { event ->
                val eventId = event["eventId"] as? String ?: ""
                if (eventId.isNotEmpty()) {
                    EventCard(
                        title = event["eventName"] as? String ?: "No Title",
                        place = event["eventLocation"] as? String ?: "No Location",
                        date = event["eventDate"] as? String ?: "No Date",
                        onClick = { onEditEventClick(eventId) }
                    )
                }
            }
        }
    }
}

@Composable
fun OfferList(onEditOfferClick: (String) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val offers = remember { mutableStateOf(listOf<Map<String, Any>>()) }
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(currentUser?.uid) {
        if (currentUser != null) {
            try {
                val offerDocs = db.collection("offers")
                    .whereEqualTo("userUID", currentUser.uid)
                    .get()
                    .await()
                // Mapear los datos incluyendo el ID del documento
                offers.value = offerDocs.documents.map { doc ->
                    doc.data?.toMutableMap()?.also { it["offerId"] = doc.id } ?: emptyMap()
                }
            } catch (e: Exception) {
                // Manejar el error si ocurre
            }
        }
    }

    // Mostrar las ofertas en la interfaz
    Column {
        if (offers.value.isEmpty()) {
            Text("No offers found", modifier = Modifier.padding(16.dp))
        } else {
            offers.value.forEach { offer ->
                val offerId = offer["offerId"] as? String ?: ""
                if (offerId.isNotEmpty()) {
                    OfferCard(
                        title = offer["offerName"] as? String ?: "No Title",
                        place = offer["offerDetails"] as? String ?: "No Details",
                        date = "",
                        onClick = { onEditOfferClick(offerId) }
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(title: String, place: String, date: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },  // Añadido onClick para navegar a la edición
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
fun OfferCard(title: String, place: String, date: String, onClick: () -> Unit) {
    EventCard(title, place, date, onClick)
}
