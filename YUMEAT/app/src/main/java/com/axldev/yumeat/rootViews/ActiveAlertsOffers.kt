package com.axldev.yumeat.rootViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.axldev.yumeat.ConfirmDeleteDialog
import com.axldev.yumeat.EventCard
import com.axldev.yumeat.OfferCard
import com.axldev.yumeat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveAlertsOffersScreen(
    onLogoutClick: () -> Unit,
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onDeleteOfferClick: (String) -> Unit,
    onDeleteEventClick: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    var selectedTab by remember { mutableStateOf("Offers") } // Default selected tab
    var offers by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var events by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var loading by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Cargar datos desde Firebase
    LaunchedEffect(selectedTab) {
        loading = true
        try {
            if (selectedTab == "Offers") {
                val offerDocs = db.collection("offers").get().await()
                offers = offerDocs.documents.map { doc ->
                    doc.data?.toMutableMap()?.also { it["offerId"] = doc.id } ?: emptyMap()
                }
            } else if (selectedTab == "Events") {
                val eventDocs = db.collection("events").get().await()
                events = eventDocs.documents.map { doc ->
                    doc.data?.toMutableMap()?.also { it["eventId"] = doc.id } ?: emptyMap()
                }
            }
        } catch (e: Exception) {
            scope.launch {
                snackbarHostState.showSnackbar("Error loading data")
            }
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Active Events & Offers",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 28.dp, top = 56.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    IconButton(
                        onClick = {
                            auth.signOut()
                            onLogoutClick()
                        }
                    ) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = Color.Gray)
                    }
                }
            )
        },
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
                        onClick = onHomeClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = Color.Gray
                        )
                    }
                    IconButton(
                        onClick = onOffersClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.LocalOffer,
                            contentDescription = "Offers",
                            tint = Color.Gray
                        )
                    }
                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "Profile",
                            tint = Color.Gray
                        )
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Pestañas de selección
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
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

                // Mostrar lista según pestaña seleccionada
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    if (selectedTab == "Offers" && offers.isEmpty()) {
                        Text(
                            text = "No active offers found.",
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else if (selectedTab == "Events" && events.isEmpty()) {
                        Text(
                            text = "No active events found.",
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val itemsToShow = if (selectedTab == "Offers") offers else events
                            items(itemsToShow) { item ->
                                val title = item["offerName"] as? String
                                    ?: item["eventName"] as? String ?: "No Title"
                                val details = item["offerDetails"] as? String
                                    ?: item["eventLocation"] as? String ?: "No Details"
                                val date = item["eventDate"] as? String ?: ""

                                if (selectedTab == "Offers") {
                                    OfferCard(
                                        title = title,
                                        place = details,
                                        date = date,
                                        onClick = {}, // No hace nada
                                        onDeleteClick = {
                                            val offerId = item["offerId"] as String
                                            onDeleteOfferClick(offerId)
                                            // Refrescar lista después de eliminar
                                            scope.launch {
                                                try {
                                                    val offerDocs = db.collection("offers").get().await()
                                                    offers = offerDocs.documents.map { doc ->
                                                        doc.data?.toMutableMap()?.also { it["offerId"] = doc.id } ?: emptyMap()
                                                    }
                                                } catch (e: Exception) {
                                                    snackbarHostState.showSnackbar("Error refreshing offers")
                                                }
                                            }
                                        }
                                    )
                                } else {
                                    EventCard(
                                        title = title,
                                        place = details,
                                        date = date,
                                        onClick = {}, // No hace nada
                                        onDeleteClick = {
                                            val eventId = item["eventId"] as String
                                            onDeleteEventClick(eventId)
                                            // Refrescar lista después de eliminar
                                            scope.launch {
                                                try {
                                                    val eventDocs = db.collection("events").get().await()
                                                    events = eventDocs.documents.map { doc ->
                                                        doc.data?.toMutableMap()?.also { it["eventId"] = doc.id } ?: emptyMap()
                                                    }
                                                } catch (e: Exception) {
                                                    snackbarHostState.showSnackbar("Error refreshing events")
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }


                    }
                }
            }
        }
    }
}
