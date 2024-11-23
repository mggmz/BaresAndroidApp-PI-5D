package com.axldev.yumeat.clientViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodieMainFeed() {
    // Estructura principal con Scaffold
    Scaffold(
        bottomBar = { BottomNavigationBar() }, // Acopla el BottomBar
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFFFFFFF)) // Fondo blanco
        ) {
            // Header
            HeaderSection()

            // Search Bar & Filters
            SearchAndFilterSection()

            // Restaurant Grid
            RestaurantGrid()
        }
    }
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Text(
            text = "YumEat",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = "Get some food!!",
            fontSize = 16.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Manzanillo, Colima",
            fontSize = 14.sp,
            color = Color(0xFF7B7B7B),
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAndFilterSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = Color(0xFFF7EBFF), // SearchBar
                    shape = RoundedCornerShape(12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter Icon",
                tint = Color.Gray,
                modifier = Modifier.padding(start = 12.dp)
            )
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

        // Selected Filters
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Chip(text = "Sushi")
            Spacer(modifier = Modifier.width(8.dp))
            Chip(text = "Some filter")
        }
    }
}

@Composable
fun Chip(text: String) {
    Box(
        modifier = Modifier
            .background(color = Color(0xFFE6F8F2), shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = text, color = Color.Black, fontSize = 14.sp)
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
    // Mock Data
    val restaurants = listOf(
        Restaurant("El Terral By Brisas", "Gourmet", "$$$", "10 AM - 10 PM", "1.2 km"),
        Restaurant("Oasis Ocean Club", "Gourmet", "$$", "10 AM - 10 PM", "0.5 km"),
        Restaurant("Monster Burguer", "Fast Food", "$$", "10 AM - 10 PM", "1.4 km"),
        Restaurant("Daruma", "Japanese", "$$$", "10 AM - 10 PM", "1.4 km")
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
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Dummy Image for Preview
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = restaurant.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = restaurant.type, color = Color.Gray, fontSize = 14.sp)
            Text(text = restaurant.hours, color = Color.Gray, fontSize = 14.sp)
            Text(text = restaurant.distance, color = Color.Blue, fontSize = 14.sp)
        }
    }
}

@Composable
fun BottomNavigationBar() {
    BottomAppBar(
        containerColor = Color(0xFFE9E9E9),
        contentColor = Color.Gray,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { /* TODO */ }) {
            Icon(imageVector = Icons.Default.Star, contentDescription = "Favorites")
        }
        IconButton(onClick = { /* TODO */ }) {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        }
        IconButton(onClick = { /* TODO */ }) {
            Icon(imageVector = Icons.Default.LocalOffer, contentDescription = "Offers")
        }
        IconButton(onClick = { /* TODO */ }) {
            Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodieMainFeedPreview() {
    FoodieMainFeed()
}