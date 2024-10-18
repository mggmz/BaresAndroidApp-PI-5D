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
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

@Composable
fun OwnerMainScreenContent(
    onAddBusinessClick: () -> Unit,
    onAddOfferClick: () -> Unit,  // Agregado para manejar el clic en el ícono de la etiqueta
    onLogoutClick: () -> Unit  // Parámetro para redirigir a la pantalla de login después de cerrar sesión
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    val currentUser = auth.currentUser
    val userUID = currentUser?.uid

    var username by remember { mutableStateOf<String?>("@Your") }  // Estado para el username
    var businesses by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var loading by remember { mutableStateOf(true) }

    // Para manejar la consulta en segundo plano
    LaunchedEffect(userUID) {
        if (userUID != null) {
            try {
                // Obtener el username desde Firestore
                val userDoc = db.collection("users").document(userUID).get().await()
                username = userDoc.getString("username")?.let { "@$it's" } ?: "@Your"

                // Obtener los negocios del usuario
                val businessDocs = db.collection("business")
                    .whereEqualTo("userUID", userUID)
                    .get()
                    .await()

                businesses = businessDocs.map { it.data }
                loading = false
            } catch (e: Exception) {
                loading = false
                // Manejo de errores si es necesario
            }
        }
    }

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
                        onClick = { /* Acción para Home */ },
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
                            .padding(top = 10.dp),

                    ) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = Color.Gray)
                    }

                    // Imagen del logo en lugar de texto "Yum Eat"
                    Image(
                        painter = painterResource(id = R.drawable.applogo), // Asegúrate de que el logo esté en la carpeta drawable
                        contentDescription = "Yum Eat Logo",
                        modifier = Modifier
                            .size(225.dp)
                            .padding(start = 70.dp, top = 0.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Text(
                    // Muestra el username o "Your" si no está disponible
                    text = "$username Businesses",
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

            // FloatingActionButton más grande y alineado a la derecha
            FloatingActionButton(
                onClick = onAddBusinessClick,
                shape = CircleShape,
                containerColor = Color(0xFF0072A3),
                modifier = Modifier
                    .size(100.dp)  // Aumentar el tamaño en un 10%
                    .align(Alignment.BottomEnd)  // Alinearlo a la parte inferior derecha
                    .padding(16.dp)  // Añadir padding para separarlo de los bordes
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Business", tint = Color.White)
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
