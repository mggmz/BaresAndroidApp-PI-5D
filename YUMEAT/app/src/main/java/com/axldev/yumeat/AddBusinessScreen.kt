package com.axldev.yumeat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import android.widget.Toast
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.*

@Composable
fun AddBusinessScreen(
    onBusinessAdded: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToOffers: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var foodType by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var openAt by remember { mutableStateOf("") }
    var closeAt by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf("") }

    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val storage = FirebaseStorage.getInstance().reference

    val currentUser = auth.currentUser

    // Launcher para abrir la galería
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
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
                text = "Add Business",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
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

            // Botón para seleccionar una imagen
            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = "Select Image")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar la imagen seleccionada
            selectedImageUri?.let {
                Image(
                    bitmap = android.graphics.BitmapFactory.decodeStream(
                        context.contentResolver.openInputStream(it)
                    ).asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón para agregar el negocio
            Button(
                onClick = {
                    if (name.isNotEmpty() && foodType.isNotEmpty() && address.isNotEmpty() && openAt.isNotEmpty() && closeAt.isNotEmpty()) {
                        if (currentUser != null) {
                            val userUID = currentUser.uid

                            if (selectedImageUri != null) {
                                val fileName = UUID.randomUUID().toString() + ".jpg"
                                val imageRef = storage.child("businesses/$fileName")

                                // Subir la imagen a Firebase Storage
                                val uploadTask = imageRef.putFile(selectedImageUri!!)
                                uploadTask.continueWithTask { task ->
                                    if (!task.isSuccessful) {
                                        task.exception?.let { throw it }
                                    }
                                    imageRef.downloadUrl
                                }.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        imageUrl = task.result.toString()

                                        // Guardar la información del negocio con la URL de la imagen
                                        val business = hashMapOf(
                                            "name" to name,
                                            "foodType" to foodType,
                                            "address" to address,
                                            "openAt" to openAt,
                                            "closeAt" to closeAt,
                                            "imageUrl" to imageUrl,
                                            "userUID" to userUID
                                        )

                                        db.collection("business")
                                            .add(business)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Business added successfully", Toast.LENGTH_SHORT).show()
                                                onBusinessAdded()
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(context, "Error adding business: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Please select an image", Toast.LENGTH_SHORT).show()
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
                Text(text = "Add Business", color = Color.White)
            }
        }
    }
}
