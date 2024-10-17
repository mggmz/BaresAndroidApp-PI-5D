package com.axldev.yumeat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
// Importaciones adicionales
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun OwnerMainScreenContent(
    onAddBusinessClick: () -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val currentUser = auth.currentUser
    val userUID = currentUser?.uid

    var businesses by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var loading by remember { mutableStateOf(true) }

    // Para manejar la consulta en segundo plano
    val coroutineScope = rememberCoroutineScope()

    // Hacer la consulta a Firestore cuando se carga la pantalla
    LaunchedEffect(userUID) {
        if (userUID != null) {
            db.collection("business")
                .whereEqualTo("userUID", userUID)
                .get()
                .addOnSuccessListener { documents ->
                    businesses = documents.map { it.data }
                    loading = false
                }
                .addOnFailureListener {
                    loading = false
                }
        }
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFFE9E9E9),
                content = {
                    IconButton(onClick = { /* Home logic */ }) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { /* Location logic */ }) {
                        Icon(Icons.Filled.Place, contentDescription = "Location")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddBusinessClick,
                shape = CircleShape,
                containerColor = Color(0xFF0072A3)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Business", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Yum Eat",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(
                text = "Your Businesses",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Mostrar "Loading" mientras se cargan los negocios
            if (loading) {
                CircularProgressIndicator()
            } else if (businesses.isEmpty()) {
                Text(text = "You have no businesses", color = Color.Gray)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(businesses) { business ->
                        BusinessCard(
                            name = business["name"] as String,
                            address = business["address"] as String,
                            foodType = business["foodType"] as String
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BusinessCard(name: String, address: String, foodType: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .graphicsLayer {
                shadowElevation = 8.dp.toPx()
                shape = RoundedCornerShape(16.dp)
                clip = true
            }
            .background(Color.White, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Imagen de ejemplo
            Image(
                painter = painterResource(id = R.drawable.food_image), // Reemplaza con un recurso de imagen adecuado
                contentDescription = "Business Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Crop
            )
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                text = address,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(text = "Type: $foodType", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
