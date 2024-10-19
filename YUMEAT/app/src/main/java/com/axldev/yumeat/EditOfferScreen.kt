package com.axldev.yumeat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

@Composable
fun EditOfferScreen(
    offerId: String,
    onOfferUpdated: () -> Unit,
    onOfferDeleted: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToOffers: () -> Unit
) {
    var offerName by remember { mutableStateOf("") }
    var offerDetails by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    // Cargar los datos de la oferta
    LaunchedEffect(offerId) {
        val offerDoc = db.collection("offers").document(offerId).get().await()
        offerName = offerDoc.getString("offerName") ?: ""
        offerDetails = offerDoc.getString("offerDetails") ?: ""
    }

    val currentUser = auth.currentUser

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
                        onClick = onNavigateToHome,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.Gray)
                    }
                    IconButton(
                        onClick = onNavigateToOffers,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Filled.LocalOffer, contentDescription = "Offers", tint = Color.Gray)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(Color.White)
        ) {
            Text(
                text = "Edit Offer",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 12.dp, top = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Offer Name Field
            OutlinedTextField(
                value = offerName,
                onValueChange = { offerName = it },
                label = { Text("Offer Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Offer Details Field
            OutlinedTextField(
                value = offerDetails,
                onValueChange = { offerDetails = it },
                label = { Text("Offer Details") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Update Offer Button
            Button(
                onClick = {
                    if (offerName.isNotEmpty() && offerDetails.isNotEmpty()) {
                        val offerUpdates = mapOf(
                            "offerName" to offerName,
                            "offerDetails" to offerDetails
                        )
                        db.collection("offers").document(offerId).update(offerUpdates).addOnSuccessListener {
                            Toast.makeText(context, "Offer updated successfully", Toast.LENGTH_SHORT).show()
                            onOfferUpdated()
                        }.addOnFailureListener { e ->
                            Toast.makeText(context, "Error updating offer: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0072A3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Update Offer", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delete Offer Button
            Button(
                onClick = {
                    db.collection("offers").document(offerId).delete().addOnSuccessListener {
                        Toast.makeText(context, "Offer deleted successfully", Toast.LENGTH_SHORT).show()
                        onOfferDeleted()
                    }.addOnFailureListener { e ->
                        Toast.makeText(context, "Error deleting offer: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Delete Offer", color = Color.White)
            }
        }
    }
}
