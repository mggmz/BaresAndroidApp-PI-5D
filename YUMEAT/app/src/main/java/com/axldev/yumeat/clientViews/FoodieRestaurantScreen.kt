package com.axldev.yumeat.clientViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.axldev.yumeat.R
import coil.compose.rememberImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun FoodieRestaurantScreen(
    businessId: String, // ID del restaurante para cargar datos dinámicos
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLikesClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    var business by remember { mutableStateOf<Map<String, Any>?>(null) }
    var promotion by remember { mutableStateOf<Map<String, Any>?>(null) }

    // Cargar información del restaurante y promociones
    LaunchedEffect(businessId) {
        val doc = db.collection("business").document(businessId).get().await()
        business = doc.data

        // Buscar promociones o eventos relacionados
        val keyword = (business?.get("name") as? String)?.split(" ")?.firstOrNull() ?: ""
        db.collection("offers")
            .whereArrayContains("keywords", keyword)
            .limit(1)
            .get()
            .addOnSuccessListener { offersSnapshot ->
                if (!offersSnapshot.isEmpty) {
                    promotion = offersSnapshot.documents[0].data
                } else {
                    db.collection("events")
                        .whereArrayContains("keywords", keyword)
                        .limit(1)
                        .get()
                        .addOnSuccessListener { eventsSnapshot ->
                            if (!eventsSnapshot.isEmpty) {
                                promotion = eventsSnapshot.documents[0].data
                            }
                        }
                }
            }
    }

    Scaffold(
        bottomBar = {
            // Barra de navegación inferior específica para esta pantalla
            RestaurantBottomNavigationBar(
                onHomeClick = onHomeClick,
                onOffersClick = onOffersClick,
                onProfileClick = onProfileClick,
                onLikesClick = onLikesClick
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(paddingValues)
        ) {
            // Imagen de fondo parcial
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                val imageUrl = business?.get("imageUrl") as? String
                if (imageUrl != null) {
                    Image(
                        painter = rememberImagePainter(imageUrl),
                        contentDescription = "Restaurant Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.placeholder_image2),
                        contentDescription = "Restaurant Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(48.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }

            // Información principal del restaurante
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = business?.get("name") as? String ?: "Nombre no disponible",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = business?.get("address") as? String ?: "Dirección no disponible",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))



            Spacer(modifier = Modifier.height(16.dp))

            // Evento o Promoción del restaurante
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

            }

            // Información adicional
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Información",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Horario: ${business?.get("openAt") ?: "N/A"} - ${business?.get("closeAt") ?: "N/A"}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun RestaurantBottomNavigationBar(
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLikesClick: () -> Unit
) {
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
            IconButton(onClick = onHomeClick) {
                Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.Gray)
            }
            IconButton(onClick = onOffersClick) {
                Icon(Icons.Filled.LocalOffer, contentDescription = "Offers", tint = Color.Gray)
            }

            IconButton(onClick = onProfileClick) {
                Icon(Icons.Filled.Person, contentDescription = "Profile", tint = Color.Gray)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodieRestaurantScreenPreview() {
    FoodieRestaurantScreen(
        businessId = "sampleBusinessId",
        onHomeClick = {},
        onOffersClick = {},
        onProfileClick = {},
        onLikesClick = {},
        onBackClick = {}
    )
}
