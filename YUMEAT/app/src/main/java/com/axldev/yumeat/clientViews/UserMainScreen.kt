package com.axldev.yumeat.clientViews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

@Composable
fun UserMainScreenContent(
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = onHomeClick,
                onOffersClick = onOffersClick,
                onProfileClick = onProfileClick
            )
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Welcome to YumEat!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "....",
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit
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
                Icons.Filled.Favorite,
                contentDescription = "Favorites",
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

@Preview(showBackground = true)
@Composable
fun UserMainScreenPreview() {
    UserMainScreenContent(
        onHomeClick = { /* No action for preview */ },
        onOffersClick = { /* No action for preview */ },
        onProfileClick = { /* No action for preview */ }
    )
}
