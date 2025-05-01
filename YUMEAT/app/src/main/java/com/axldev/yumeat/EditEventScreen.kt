package com.axldev.yumeat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.draw.clip
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.tasks.await
import java.util.*

@Composable
fun EditEventScreen(
    eventId: String,
    onEventUpdated: () -> Unit,
    onEventDeleted: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToOffers: () -> Unit
) {
    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    // Cargar los datos del evento
    LaunchedEffect(eventId) {
        val eventDoc = db.collection("events").document(eventId).get().await()
        eventName = eventDoc.getString("eventName") ?: ""
        eventLocation = eventDoc.getString("eventLocation") ?: ""
        eventDate = eventDoc.getString("eventDate") ?: ""
    }

    val currentUser = auth.currentUser

    // Instancia del calendario para DatePicker
    val calendar = Calendar.getInstance()
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    // Función para actualizar la fecha seleccionada
    fun updateSelectedDate(year: Int, month: Int, day: Int) {
        eventDate = "$day/${month + 1}/$year"
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
                text = "Edit Event",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 12.dp, top = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Event Name Field
            OutlinedTextField(
                value = eventName,
                onValueChange = { eventName = it },
                label = { Text("Event Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Event Location Field
            OutlinedTextField(
                value = eventLocation,
                onValueChange = { eventLocation = it },
                label = { Text("Event Location") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Event Date Field
            OutlinedTextField(
                value = eventDate,
                onValueChange = { eventDate = it },
                label = { Text("Select Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            // DatePicker
            Spacer(modifier = Modifier.height(16.dp))
            DatePickerView(
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

            // Update Event Button
            Button(
                onClick = {
                    if (eventName.isNotEmpty() && eventLocation.isNotEmpty() && eventDate.isNotEmpty()) {
                        val eventUpdates = mapOf(
                            "eventName" to eventName,
                            "eventLocation" to eventLocation,
                            "eventDate" to eventDate
                        )
                        db.collection("events").document(eventId).update(eventUpdates).addOnSuccessListener {
                            Toast.makeText(context, "Event updated successfully", Toast.LENGTH_SHORT).show()
                            onEventUpdated()
                        }.addOnFailureListener { e ->
                            Toast.makeText(context, "Error updating event: ${e.message}", Toast.LENGTH_SHORT).show()
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
                Text(text = "Update Event", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delete Event Button
            Button(
                onClick = {
                    db.collection("events").document(eventId).delete().addOnSuccessListener {
                        Toast.makeText(context, "Event deleted successfully", Toast.LENGTH_SHORT).show()
                        onEventDeleted()
                    }.addOnFailureListener { e ->
                        Toast.makeText(context, "Error deleting event: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Delete Event", color = Color.White)
            }
        }
    }
}
