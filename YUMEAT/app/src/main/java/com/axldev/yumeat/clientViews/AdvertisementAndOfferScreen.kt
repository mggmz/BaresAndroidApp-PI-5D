package com.axldev.yumeat.clientViews

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import coil.compose.rememberImagePainter
import com.axldev.yumeat.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import android.util.Log
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AnimatedSelectionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        modifier = modifier
            .scale(scale)
            .background(
                color = if (isSelected) Color(0xFFFFA500) else Color.Transparent,
                shape = RoundedCornerShape(50)
            )
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.Black,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
        )
    }
}

@Composable
fun AdvertisementAndOfferScreen(
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLikesClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var currentView by remember { mutableStateOf("Advertisement") }
    var advertisements by remember { mutableStateOf(listOf<Map<String, Any>>()) }
    var offers by remember { mutableStateOf(listOf<Map<String, Any>>()) }

    val db = FirebaseFirestore.getInstance()
    var adsListenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }
    var offersListenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }

    // Load advertisements (events) from Firebase
    LaunchedEffect(Unit) {
        adsListenerRegistration = db.collection("events")
            .whereEqualTo("active", true)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("AdvertisementScreen", "Error fetching events: ${e.message}")
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    advertisements = snapshots.documents.mapNotNull { doc ->
                        val data = doc.data
                        data?.also { it["id"] = doc.id }
                    }
                }
            }
    }

    // Load offers from Firebase
    LaunchedEffect(Unit) {
        offersListenerRegistration = db.collection("offers")
            .whereEqualTo("active", true)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.e("AdvertisementScreen", "Error fetching offers: ${e.message}")
                    return@addSnapshotListener
                }
                if (snapshots != null) {
                    offers = snapshots.documents.mapNotNull { doc ->
                        val data = doc.data
                        data?.also { it["id"] = doc.id }
                    }
                }
            }
    }

    // Cancel the listeners when the Composable is destroyed
    DisposableEffect(Unit) {
        onDispose {
            adsListenerRegistration?.remove()
            offersListenerRegistration?.remove()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = onLogoutClick) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = "Logout",
                                tint = Color.Gray
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.applogo),
                            contentDescription = "YumEat Logo",
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                },
                backgroundColor = Color.White,
                elevation = 4.dp
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = onHomeClick,
                onOffersClick = onOffersClick,
                onProfileClick = onProfileClick,
                onLikesClick = onLikesClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(50),
                    elevation = 4.dp,
                    backgroundColor = Color.LightGray
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        AnimatedSelectionButton(
                            text = "Advertisement",
                            isSelected = currentView == "Advertisement",
                            onClick = { currentView = "Advertisement" },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )

                        AnimatedSelectionButton(
                            text = "Offer",
                            isSelected = currentView == "Offer",
                            onClick = { currentView = "Offer" },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (currentView) {
                    "Advertisement" -> {
                        if (advertisements.isEmpty()) {
                            Text(
                                text = "No advertisements available",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(advertisements) { ad ->
                                    AdvertisementItem(advertisement = ad)
                                }
                            }
                        }
                    }
                    "Offer" -> {
                        if (offers.isEmpty()) {
                            Text(
                                text = "No offers available",
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(offers) { offer ->
                                    OfferItem(offer = offer)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdvertisementItem(advertisement: Map<String, Any>) {
    val title = advertisement["eventName"] as? String ?: "No Title"
    val description = advertisement["eventDescription"] as? String ?: "No Description"
    val imageUrl = advertisement["imageUrl"] as? String
    val businessName = advertisement["businessName"] as? String ?: "Unknown Business"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Display image if available
            if (imageUrl != null) {
                Image(
                    painter = rememberImagePainter(data = imageUrl),
                    contentDescription = "Event Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Business: $businessName",
                style = MaterialTheme.typography.subtitle2,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun OfferItem(offer: Map<String, Any>) {
    val title = offer["offerName"] as? String ?: "No Title"
    val description = offer["offerDetails"] as? String ?: "No Description"
    val imageUrl = offer["imageUrl"] as? String
    val businessName = offer["businessName"] as? String ?: "Unknown Business"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Display image if available
            if (imageUrl != null) {
                Image(
                    painter = rememberImagePainter(data = imageUrl),
                    contentDescription = "Offer Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Business: $businessName",
                style = MaterialTheme.typography.subtitle2,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
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
                onClick = onLikesClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Likes",
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
}

@Preview(showBackground = true)
@Composable
fun AdvertisementAndOfferPreview() {
    AdvertisementAndOfferScreen(
        onHomeClick = { /* No action for preview */ },
        onOffersClick = { /* No action for preview */ },
        onProfileClick = { /* No action for preview */ },
        onLikesClick = { /* No action for preview */ },
        onLogoutClick = { /* No action for preview */ }
    )
}
