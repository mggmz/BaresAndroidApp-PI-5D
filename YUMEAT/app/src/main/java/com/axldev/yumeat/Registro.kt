package com.axldev.yumeat

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Register", fontSize = 32.sp, modifier = Modifier.padding(bottom = 32.dp))

        // Campos de registro
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { /* Acción de registro */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Register")
        }
    }
}
