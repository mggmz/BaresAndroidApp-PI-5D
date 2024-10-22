package com.axldev.yumeat

import android.widget.DatePicker
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.draw.clip
import java.util.*

@Composable
fun AddOfferScreen(
    onOfferAdded: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToOffers: () -> Unit
) {
    var offerName by remember { mutableStateOf("") }
    var offerDetails by remember { mutableStateOf("") }
    var offerDate by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    val currentUser = auth.currentUser

    // Instancia del calendario para DatePicker
    val calendar = Calendar.getInstance()
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    // Función para actualizar la fecha seleccionada
    fun updateSelectedDate(year: Int, month: Int, day: Int) {
        offerDate = "$day/${month + 1}/$year"
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
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Add Offer",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Offer Date Field (mostrar solo el placeholder)
            OutlinedTextField(
                value = offerDate,
                onValueChange = { offerDate = it },
                label = { Text("Select Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar el DatePicker fijo debajo del campo de fecha
            DatePickerOfferView(
                year = selectedYear,
                month = selectedMonth,
                day = selectedDay,
                onDateChange = { year, month, day ->
                    selectedYear = year
                    selectedMonth = month
                    selectedDay = day
                    updateSelectedDate(year, month, day)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Add Offer Button
            Button(
                onClick = {
                    if (offerName.isNotEmpty() && offerDetails.isNotEmpty() && offerDate.isNotEmpty()) {
                        if (currentUser != null) {
                            val userUID = currentUser.uid
                            val offer = hashMapOf(
                                "offerName" to offerName,
                                "offerDetails" to offerDetails,
                                "offerDate" to offerDate,
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
                    .height(48.dp)
            ) {
                Text(text = "Add Offer", color = Color.White)
            }
        }
    }
}

@Composable
fun DatePickerOfferView(
    year: Int,
    month: Int,
    day: Int,
    onDateChange: (Int, Int, Int) -> Unit
) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        factory = { context ->
            DatePicker(context).apply {
                init(year, month, day) { _, selectedYear, selectedMonth, selectedDay ->
                    onDateChange(selectedYear, selectedMonth, selectedDay)
                }
            }
        }
    )
}
