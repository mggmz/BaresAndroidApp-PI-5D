package com.axldev.yumeat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegistroScreen(navController: NavController) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Input para email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input para password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mensaje de error
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botón de registro
        Button(
            onClick = {
                if (isValidBusinessOwner(email.text)) {
                    // Redirigir a la pantalla del Business Owner si el email es válido
                    navController.navigate("owner_main")
                } else {
                    // Redirigir a otra pantalla o manejar el error
                    errorMessage = "Invalid Business Owner email"
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(text = "Register")
        }
    }
}

// Función para validar si el email corresponde a un Business Owner
fun isValidBusinessOwner(email: String): Boolean {
    // Aquí puedes poner la lógica real de validación, por ejemplo:
    // Comprobar si el email pertenece a un dominio específico o si está en una lista de Business Owners
    return email.endsWith("@businessowner.com") // Ejemplo simple
}
