package com.axldev.yumeat


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisteredPlacesScreen() {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val currentUser = auth.currentUser
    var places by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var loading by remember { mutableStateOf(true) }

    // Obtener negocios desde Firestore
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            try {
                val placeDocs = db.collection("business")
                    .whereEqualTo("userUID", currentUser.uid)
                    .get()
                    .await()
                places = placeDocs.documents.map { doc ->
                    doc.data?.toMutableMap()?.also { it["placeId"] = doc.id } ?: emptyMap()
                }
                loading = false
            } catch (e: Exception) {
                loading = false
                // Manejar el error si es necesario
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registered Places", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )

        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(innerPadding)
        ) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (places.isEmpty()) {
                Text(
                    "No registered places found.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    places.forEach { place ->
                        val placeId = place["placeId"] as? String ?: return@forEach
                        PlaceCard(
                            name = place["name"] as? String ?: "Unknown Place",
                            address = place["address"] as? String ?: "Unknown Address",
                            imageUrl = place["imageUrl"] as? String,
                            date = "Added at: ${(place["createdAt"] as? Long)?.let { formatDate(it) } ?: "Unknown Date"}",
                            onDeleteClick = {
                                db.collection("business").document(placeId).delete()
                                    .addOnSuccessListener {
                                        places = places.filterNot { it["placeId"] == placeId }
                                    }
                                    .addOnFailureListener {
                                        // Manejar el error al eliminar
                                    }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceCard(
    name: String,
    address: String,
    imageUrl: String?,
    date: String,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Acción al hacer clic, como editar */ },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del negocio desde Firebase (imageUrl)
            Image(
                painter = if (imageUrl != null) rememberImagePainter(imageUrl) else painterResource(R.drawable.placeholder_image),
                contentDescription = "Place Image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = address, fontSize = 14.sp, color = Color.Gray)
                Text(text = date, fontSize = 12.sp, color = Color.LightGray)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Place", tint = Color.Red)
            }
        }
    }
}

// Función para formatear la fecha desde un timestamp
fun formatDate(timestamp: Long): String {
    val date = java.util.Date(timestamp)
    val format = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
    return format.format(date)
}
