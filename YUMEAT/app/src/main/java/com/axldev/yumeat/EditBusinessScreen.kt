package com.axldev.yumeat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun EditBusinessScreen(
    businessId: String,
    onBusinessUpdated: () -> Unit,
    onBusinessDeleted: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToOffers: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var foodType by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var openAt by remember { mutableStateOf("") }
    var closeAt by remember { mutableStateOf("") }

    val context = LocalContext.current // Obtenemos el contexto aquí
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(businessId) {
        // Obtener los datos del negocio para pre-poblar los campos
        val businessDoc = db.collection("business").document(businessId).get().await()
        name = businessDoc.getString("name") ?: ""
        foodType = businessDoc.getString("foodType") ?: ""
        address = businessDoc.getString("address") ?: ""
        openAt = businessDoc.getString("openAt") ?: ""
        closeAt = businessDoc.getString("closeAt") ?: ""
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
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Edit Business",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp, top = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Nombre del negocio
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tipo de comida
            OutlinedTextField(
                value = foodType,
                onValueChange = { foodType = it },
                label = { Text("Food Type") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dirección
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Horario de apertura y cierre
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
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

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para actualizar el negocio
            Button(
                onClick = {
                    val businessUpdates = mapOf(
                        "name" to name,
                        "foodType" to foodType,
                        "address" to address,
                        "openAt" to openAt,
                        "closeAt" to closeAt
                    )
                    db.collection("business").document(businessId).update(businessUpdates).addOnSuccessListener {
                        Toast.makeText(context, "Business updated successfully", Toast.LENGTH_SHORT).show()
                        onBusinessUpdated()
                    }.addOnFailureListener { e ->
                        Toast.makeText(context, "Error updating business: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0072A3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Update Business", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para eliminar el negocio
            Button(
                onClick = {
                    db.collection("business").document(businessId).delete().addOnSuccessListener {
                        Toast.makeText(context, "Business deleted successfully", Toast.LENGTH_SHORT).show()
                        onBusinessDeleted()
                    }.addOnFailureListener { e ->
                        Toast.makeText(context, "Error deleting business: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Delete Business", color = Color.White)
            }
        }
    }
}
