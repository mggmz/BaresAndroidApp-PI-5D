package com.axldev.yumeat.clientViews

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.axldev.yumeat.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

@Composable
fun FoodieMainFeed(
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLikesClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onRestaurantClick: (String) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    var businesses by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }

    // Cargar negocios desde Firebase y escuchar cambios en tiempo real
    LaunchedEffect(Unit) {
        listenerRegistration = db.collection("business")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    businesses = snapshots.documents.mapNotNull { doc ->
                        val data = doc.data as MutableMap<String, Any>?
                        data?.also { it["id"] = doc.id }
                    }
                }
            }
    }

    DisposableEffect(Unit) {
        onDispose {
            listenerRegistration?.remove()
        }
    }

    Scaffold(
        topBar = {
            TopBar(onLogoutClick = onLogoutClick)
        },
        bottomBar = {
            MainFeedBottomNavigationBar(
                onHomeClick = onHomeClick,
                onOffersClick = onOffersClick,
                onProfileClick = onProfileClick,
                onLikesClick = onLikesClick
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFFFFFFF))
        ) {
            SearchBar(searchText, onSearchTextChanged = { newValue ->
                searchText = newValue
            })

            BusinessGrid(
                businesses = businesses,
                searchText = searchText.text,
                onRestaurantClick = onRestaurantClick
            )
        }
    }
}

@Composable
fun TopBar(onLogoutClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.applogo),
            contentDescription = "YumEat Logo",
            modifier = Modifier
                .size(135.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Fit
        )

        IconButton(
            onClick = onLogoutClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 8.dp)
        ) {
            Icon(Icons.Filled.ExitToApp, contentDescription = "Logout", tint = Color.Gray)
        }
    }
}

@Composable
fun MainFeedBottomNavigationBar(
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLikesClick: () -> Unit
) {
    BottomAppBar(
        backgroundColor = Color(0xFFE0E0E0),
        cutoutShape = CircleShape,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
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

@Composable
fun SearchBar(searchText: TextFieldValue, onSearchTextChanged: (TextFieldValue) -> Unit) {
    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        placeholder = { Text("Buscar") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true,
        shape = RoundedCornerShape(32.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFFFFA500),
            cursorColor = Color(0xFFFFA500)
        )
    )
}

@Composable
fun BusinessGrid(
    businesses: List<Map<String, Any>>,
    searchText: String,
    onRestaurantClick: (String) -> Unit
) {
    val filteredBusinesses = businesses.filter { business ->
        val name = (business["name"] as? String)?.lowercase() ?: ""
        val foodType = (business["foodType"] as? String)?.lowercase() ?: ""
        val address = (business["address"] as? String)?.lowercase() ?: ""
        val searchLower = searchText.lowercase()
        name.contains(searchLower) || foodType.contains(searchLower) || address.contains(searchLower)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(filteredBusinesses) { business ->
            BusinessCard(business = business, onClick = {
                val businessId = business["id"] as? String
                if (businessId != null) {
                    onRestaurantClick(businessId)
                }
            })
        }
    }
}

@Composable
fun BusinessCard(
    business: Map<String, Any>,
    onClick: () -> Unit
) {
    val name = business["name"] as? String ?: "Nombre no disponible"
    val foodType = business["foodType"] as? String ?: "Tipo de comida no disponible"
    val address = business["address"] as? String ?: "Dirección no disponible"
    val imageUrl = business["imageUrl"] as? String

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(250.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Column {
            if (imageUrl != null) {
                Image(
                    painter = rememberImagePainter(data = imageUrl),
                    contentDescription = "Imagen del negocio",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.food_image),
                    contentDescription = "Imagen de marcador de posición",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = foodType,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
                Text(
                    text = address,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodieMainFeedPreview() {
    FoodieMainFeed(
        onHomeClick = {},
        onOffersClick = {},
        onProfileClick = {},
        onLikesClick = {},
        onLogoutClick = {},
        onRestaurantClick = {}
    )
}
