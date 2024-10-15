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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn

@Composable
fun AddEventScreen() {
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var startsAt by remember { mutableStateOf("") }
    var endsAt by remember { mutableStateOf("") }

    // Scaffold para manejar el BottomAppBar
    Scaffold(
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color(0xFFE9E9E9)
            ) {
                // Botón de navegación para Home con ícono
                IconButton(onClick = { /* Lógica para ir a Home */ }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home") // Ícono para el botón Home
                }
                Spacer(Modifier.weight(1f)) // Espacio flexible para centrar el botón
                // Botón de navegación para Location con ícono
                IconButton(onClick = { /* Lógica para ir a Location */ }) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Location") // Ícono para el botón Location
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
                text = "Add Event",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            // Description Field
            Text(text = "Description", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Place/Address Field
            Text(text = "Place/Address", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Starts At Field
            Text(text = "Starts At", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = startsAt,
                onValueChange = { startsAt = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text) // Cambia el tipo de teclado según sea necesario
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ends At Field
            Text(text = "Ends At", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = endsAt,
                onValueChange = { endsAt = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text) // Cambia el tipo de teclado según sea necesario
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Image Upload Section
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Texto para agregar imágenes
                Text(text = "Add Images")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add Event Button
            Button(
                onClick = { /* Lógica para agregar evento */ },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0072A3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Add Event", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddEventScreenPreview() {
    AddEventScreen()
}
