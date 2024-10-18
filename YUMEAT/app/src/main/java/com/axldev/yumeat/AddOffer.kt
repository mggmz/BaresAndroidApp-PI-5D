package com.axldev.yumeat

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip

@Composable
fun AddOfferScreen(
    onOfferAdded: () -> Unit,
    onNavigateToHome: () -> Unit,  // Parámetro para navegar a Home
    onNavigateToOffers: () -> Unit // Parámetro para navegar a la pantalla de ofertas
) {
    var offerName by remember { mutableStateOf("") }
    var offerDetails by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

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
                        onClick = onNavigateToHome,  // Navega al Home cuando se presiona el botón
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.Gray)
                    }
                    IconButton(
                        onClick = onNavigateToOffers,  // Navega a la pantalla de ofertas
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Filled.LocalOffer, contentDescription = "Offers", tint = Color.Gray)
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Add Offer",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 12.dp, top = 28.dp)
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

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Botón en la parte inferior de la pantalla
            Button(
                onClick = {
                    if (offerName.isNotEmpty() && offerDetails.isNotEmpty()) {
                        if (currentUser != null) {
                            val userUID = currentUser.uid
                            val offer = hashMapOf(
                                "offerName" to offerName,
                                "offerDetails" to offerDetails,
                                "userUID" to userUID
                            )
                            db.collection("offers")
                                .add(offer)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Offer added successfully", Toast.LENGTH_SHORT).show()
                                    onOfferAdded()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error adding offer: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(context, "User not authenticated", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0072A3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)  // Alinea el botón en la parte inferior
                    .padding(16.dp)
                    .height(48.dp)
            ) {
                Text(text = "Add Offer", color = Color.White)
            }
        }
    }
}
