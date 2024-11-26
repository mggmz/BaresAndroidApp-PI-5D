package com.axldev.yumeat.clientViews

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberImagePainter
import com.axldev.yumeat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FoodieProfileScreen(
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLikesClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onEditInfoClick: () -> Unit // This functionality is omitted as per instructions
) {
    val backgroundColor = Color(0xFFFFFFFF)
    val buttonLogoutColor = Color(0xE8EF2D2D)

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser
    var username by remember { mutableStateOf("Username") }
    var email by remember { mutableStateOf("Email") }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    var listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }

    // Fetch user information from Firestore
    LaunchedEffect(currentUser?.uid) {
        if (currentUser != null) {
            listenerRegistration = db.collection("users")
                .document(currentUser.uid)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        // Handle the error if necessary
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        val data = snapshot.data
                        username = data?.get("username") as? String ?: "Username"
                        email = data?.get("email") as? String ?: "Email"
                        profileImageUrl = data?.get("profileImageUrl") as? String
                    } else {
                        // If the user document doesn't exist, use default values
                        username = currentUser.displayName ?: "Username"
                        email = currentUser.email ?: "Email"
                    }
                }
        }
    }

    // Remove listener when composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            listenerRegistration?.remove()
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
        modifier = Modifier.fillMaxSize(),
        backgroundColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Background Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder_image2),
                    contentDescription = "Foodie Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Profile Image
            Box(
                modifier = Modifier
                    .offset(y = (-50).dp)
                    .size(140.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                if (profileImageUrl != null) {
                    Image(
                        painter = rememberImagePainter(profileImageUrl),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .border(2.dp, backgroundColor, CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.user_placeholder2),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .border(2.dp, backgroundColor, CircleShape)
                    )
                }
            }

            // Username and Description
            Text(
                text = username,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(
                text = "Foodie",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Information Section
            Text(
                text = "Información",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = username,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = email,
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Omit Edit Information Button
            /*
            Button(
                onClick = onEditInfoClick,
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonRecoverPasswordColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Editar Información",
                    color = Color.White
                )
            }
            */

            Spacer(modifier = Modifier.height(32.dp))

            // Logout Button
            Button(
                onClick = {
                    auth.signOut()
                    onLogoutClick()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonLogoutColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Cerrar Sesión",
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodieProfileScreenPreview() {
    FoodieProfileScreen(
        onHomeClick = {},
        onOffersClick = {},
        onProfileClick = {},
        onLikesClick = {},
        onLogoutClick = {},
        onEditInfoClick = {}
    )
}
