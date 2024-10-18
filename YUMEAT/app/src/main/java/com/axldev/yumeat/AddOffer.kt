package com.axldev.yumeat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer

@Composable
fun AddOfferScreen(
    onOfferAdded: () -> Unit  // Para realizar alguna acción al agregar la oferta
) {
    var description by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }
    var validUntil by remember { mutableStateOf("") }

    // Scaffold para manejar el BottomAppBar
    Scaffold(
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color(0xFFE9E9E9)
            ) {
                // Botón de navegación para Home con ícono
                IconButton(onClick = { /* Lógica para ir a Home */ }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }
                Spacer(Modifier.weight(1f))  // Espacio flexible para centrar el botón
                // Botón de navegación para Offers con ícono
                IconButton(onClick = { /* Lógica para ir a Offers */ }) {
                    Icon(Icons.Filled.LocalOffer, contentDescription = "Offers")
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
            // Título
            Text(
                text = "Add Offer",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Description Field
            Text(text = "Offer Description", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Discount Field
            Text(text = "Discount Percentage", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = discount,
                onValueChange = { discount = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Valid Until Field
            Text(text = "Valid Until", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = validUntil,
                onValueChange = { validUntil = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add Offer Button
            Button(
                onClick = {
                    onOfferAdded()  // Ejecutar la lógica de agregar oferta
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0072A3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Add Offer", color = Color.White)
            }
        }
    }
}
