package com.axldev.yumeat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun OwnerMainScreenContent(navController: NavController) {
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
                onClick = {
                    // Navigate to the AddBusinessScreen
                    navController.navigate("add_business")
                },
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

            // Business Card with shadow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .graphicsLayer { // Adding shadow effect
                        shadowElevation = 8.dp.toPx() // Shadow elevation
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
                    Image(
                        painter = painterResource(id = R.drawable.food_image), // replace with actual image resource
                        contentDescription = "Business Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(bottom = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "Oasis Ocean Club", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(
                        text = "Calle Delfin 400, Club Santiago Manzanillo, Colima",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "4.7", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(4.dp))
                        // Add star icons or ratings UI here
                    }
                }
            }
        }
    }
}

