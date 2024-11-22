package com.axldev.yumeat.rootViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
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
import com.axldev.yumeat.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveAlertsOffersScreen(
    onDeleteOfferClick: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser
    var offers by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var loading by remember { mutableStateOf(true) }

    // Cargar ofertas desde Firebase
    LaunchedEffect(Unit) {
        if (currentUser != null) {
            try {
                val offerDocs = db.collection("offers")
                    .whereEqualTo("userUID", currentUser.uid)
                    .get()
                    .await()
                offers = offerDocs.documents.map { doc ->
                    doc.data?.toMutableMap()?.also { it["offerId"] = doc.id } ?: emptyMap()
                }
                loading = false
            } catch (e: Exception) {
                loading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Active Alerts & Offers", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                actions = {
                    IconButton(onClick = { /* Implementa acción del menú */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF6F6F6))
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (offers.isEmpty()) {
                Text(
                    text = "No active offers found.",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(offers) { offer ->
                        val offerId = offer["offerId"] as? String ?: ""
                        OfferCard(
                            name = offer["offerName"] as? String ?: "No Name",
                            details = offer["offerDetails"] as? String ?: "No Details",
                            imageUrl = offer["imageUrl"] as? String,
                            onDeleteClick = {
                                onDeleteOfferClick(offerId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OfferCard(
    name: String,
    details: String,
    imageUrl: String?,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = if (imageUrl != null) rememberImagePainter(imageUrl) else painterResource(
                    R.drawable.placeholder_image),
                contentDescription = "Offer Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = details, color = Color.Gray, fontSize = 14.sp)
            }
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Offer", tint = Color.Red)
            }
        }
    }
}




