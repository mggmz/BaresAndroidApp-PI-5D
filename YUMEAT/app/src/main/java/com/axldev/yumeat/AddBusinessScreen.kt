package com.axldev.yumeat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@Composable
fun AddBusinessScreen(
    onBusinessAdded: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var foodType by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var openAt by remember { mutableStateOf("") }
    var closeAt by remember { mutableStateOf("") }
    val context = LocalContext.current

    val db = FirebaseFirestore.getInstance() // Instancia de Firebase Firestore

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFFE9E9E9),
                content = {
                    IconButton(onClick = { /* Home logic */ }) {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    }
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { /* Navigate logic */ }) {
                        Icon(Icons.Filled.Place, contentDescription = "Location")
                    }
                }
            )
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
                text = "Add Business",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Food Type Field
            OutlinedTextField(
                value = foodType,
                onValueChange = { foodType = it },
                label = { Text("Food Type") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Address Field
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Open and Close Times
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedTextField(
                    value = openAt,
                    onValueChange = { openAt = it },
                    label = { Text("Open At") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedTextField(
                    value = closeAt,
                    onValueChange = { closeAt = it },
                    label = { Text("Close At") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add Business Button
            Button(
                onClick = {
                    if (name.isNotEmpty() && foodType.isNotEmpty() && address.isNotEmpty() && openAt.isNotEmpty() && closeAt.isNotEmpty()) {
                        val business = hashMapOf(
                            "name" to name,
                            "foodType" to foodType,
                            "address" to address,
                            "openAt" to openAt,
                            "closeAt" to closeAt
                        )

                        // Guardar en Firestore
                        db.collection("business")
                            .add(business)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Business added successfully", Toast.LENGTH_SHORT).show()
                                onBusinessAdded() // Acción después de agregar el negocio
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Error adding business: ${e.message}", Toast.LENGTH_SHORT).show()
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
                Text(text = "Add Business", color = Color.White)
            }
        }
    }
}
