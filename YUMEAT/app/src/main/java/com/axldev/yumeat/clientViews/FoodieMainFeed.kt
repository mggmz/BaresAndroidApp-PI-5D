package com.axldev.yumeat.clientViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.axldev.yumeat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodieMainFeed(
    onLogoutClick: () -> Unit,
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLikesClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Logo YumEat
                Image(
                    painter = painterResource(id = R.drawable.applogo),
                    contentDescription = "YumEat Logo",
                    modifier = Modifier
                        .size(135.dp)
                        .align(Alignment.TopCenter),
                    contentScale = ContentScale.Fit
                )

                // Icono de Logout
                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 8.dp)
                ) {
                    Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = Color.Gray)
                }
            }
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
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFFFFFF))
        ) {
            // Search Bar and Filters
            SearchAndFilterSection()

            // Restaurant Grid
            RestaurantGrid()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAndFilterSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = Color(0xFFF7EBFF),
                    shape = RoundedCornerShape(12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar un restaurante...") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

data class Restaurant(
    val name: String,
    val type: String,
    val priceRange: String,
    val hours: String,
    val distance: String
)

@Composable
fun RestaurantGrid() {
    val restaurants = listOf(
        Restaurant("El Terral By Brisas", "Gourmet", "$$$", "10 AM - 10 PM", "1.2 km")
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(restaurants) { restaurant ->
            RestaurantCard(restaurant)
        }
    }
}

@Composable
fun RestaurantCard(restaurant: Restaurant) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFFFFF))
        ) {
            Image(
                painter = rememberImagePainter(R.drawable.placeholder_image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = restaurant.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )
                Text(text = restaurant.type, color = Color.Gray, fontSize = 14.sp)
                Text(text = restaurant.hours, color = Color.Gray, fontSize = 14.sp)
                Text(text = restaurant.distance, color = Color.Blue, fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodieMainFeedPreview() {
    FoodieMainFeed(
        onLogoutClick = {},
        onHomeClick = {},
        onOffersClick = {},
        onProfileClick = {},
        onLikesClick = {}
    )
}
