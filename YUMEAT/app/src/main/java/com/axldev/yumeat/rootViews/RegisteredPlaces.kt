package com.axldev.yumeat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisteredPlacesScreen(
    onLogoutClick: () -> Unit,
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var places by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var loading by remember { mutableStateOf(true) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var placeToDelete by remember { mutableStateOf<Map<String, Any>?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Obtener todos los negocios desde Firestore
    LaunchedEffect(Unit) {
        try {
            val allPlacesDocs = db.collection("business").get().await()
            val users = db.collection("users").get().await().documents.associateBy { it.id }
            places = allPlacesDocs.documents.map { doc ->
                doc.data?.toMutableMap()?.also {
                    it["placeId"] = doc.id
                    val userUID = it["userUID"] as? String
                    it["username"] = users[userUID]?.get("username") ?: "Unknown User"
                } ?: emptyMap()
            }
            loading = false
        } catch (e: Exception) {
            loading = false
            // Manejar el error si es necesario
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
                            text = "Registered Places",
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
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (places.isEmpty()) {
                Text(
                    "No registered places found.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            } else {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    places.forEach { place ->
                        val placeId = place["placeId"] as? String ?: return@forEach
                        PlaceCard(
                            name = place["name"] as? String ?: "Unknown Place",
                            address = place["address"] as? String ?: "Unknown Address",
                            username = place["username"] as? String ?: "Unknown User",
                            imageUrl = place["imageUrl"] as? String,
                            onDeleteClick = {
                                placeToDelete = place
                                showDeleteConfirmation = true
                            }
                        )
                    }
                }
            }
        }

        // Diálogo de confirmación para eliminar un negocio
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = { Text("Delete Confirmation") },
                text = { Text("Are you sure you want to delete this place?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            placeToDelete?.let {
                                val placeId = it["placeId"] as? String ?: return@TextButton
                                db.collection("business").document(placeId).delete()
                                    .addOnSuccessListener {
                                        places = places.filterNot { place -> place["placeId"] == placeId }
                                        showDeleteConfirmation = false
                                        // Mostrar mensaje de éxito
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Place deleted successfully")
                                        }
                                    }
                                    .addOnFailureListener {
                                        showDeleteConfirmation = false
                                        // Mostrar mensaje de error
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Error deleting place")
                                        }
                                    }
                            }
                        }
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmation = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun PlaceCard(
    name: String,
    address: String,
    username: String,
    imageUrl: String?,
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
                Text(text = "Owner: $username", fontSize = 12.sp, color = Color.Gray)
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
