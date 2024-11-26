package com.axldev.yumeat.clientViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.axldev.yumeat.quicksandFont

@Composable
fun FavoritesScreen(
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLikesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onLogoutClick,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ExitToApp,
                                contentDescription = "Logout",
                                tint = Color.Gray
                            )
                        }
                        Text(
                            text = "Favorites",
                            fontFamily = quicksandFont,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                },
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
                        Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.Gray)
                    }
                    IconButton(
                        onClick = onOffersClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Filled.LocalOffer, contentDescription = "Offers", tint = Color.Gray)
                    }
                    IconButton(
                        onClick = onLikesClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Filled.Favorite, contentDescription = "Likes", tint = Color.Gray)
                    }
                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = "Profile", tint = Color.Gray)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de restaurantes favoritos
            Column(modifier = Modifier.fillMaxSize()) {
                FavoriteRestaurantCard(
                    imageRes = R.drawable.placeholder_image, // Imagen de prueba
                    name = "El Terral By Brisas",
                    distance = "5 min by car",
                    onRemoveFromFavorites = { /* Acción para eliminar */ }
                )
                Spacer(modifier = Modifier.height(16.dp))
                FavoriteRestaurantCard(
                    imageRes = R.drawable.user_placeholder, // Imagen de prueba
                    name = "Pata Salada",
                    distance = "5 min by car",
                    onRemoveFromFavorites = { /* Acción para eliminar */ }
                )
            }
        }
    }
}

@Composable
fun FavoriteRestaurantCard(
    imageRes: Int,
    name: String,
    distance: String,
    onRemoveFromFavorites: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { /* Acción al hacer clic en el restaurante */ },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)), // Fondo blanco
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Restaurant Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = onRemoveFromFavorites,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Remove from Favorites",
                        tint = Color.White
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_mylocation),
                        contentDescription = "Distance Icon",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = distance,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    FavoritesScreen(
        onHomeClick = {},
        onOffersClick = {},
        onProfileClick = {},
        onLikesClick = {},
        onLogoutClick = {}
    )
}

