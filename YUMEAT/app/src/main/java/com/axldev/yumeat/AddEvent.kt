package com.axldev.yumeat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource

@Composable
fun AddEventScreen() {
    var description by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var startsAt by remember { mutableStateOf("") }
    var endsAt by remember { mutableStateOf("") }

    // Scaffold to manage BottomAppBar
    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFFE9E9E9),
                content = {
                    // Aquí puedes agregar lógica para los botones de navegación si es necesario
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

                // Ícono de clip de oficina
                Image(
                    painter = painterResource(id = R.drawable.ic_office_clip), // Cambia 'ic_office_clip' por el nombre de tu archivo
                    contentDescription = "Office Clip Icon",
                    modifier = Modifier
                        .size(36.dp) // Tamaño del ícono
                        .padding(start = 8.dp) // Espacio a la izquierda
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Add Event Button
            Button(
                onClick = { /* Add Event logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0072A3)),
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