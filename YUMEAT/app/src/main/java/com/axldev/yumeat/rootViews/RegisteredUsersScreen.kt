package com.axldev.yumeat.rootViews



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.axldev.yumeat.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisteredUsersScreen(
    onEditUserClick: (String) -> Unit,  // Callback para editar usuario
    onDeleteUserClick: (String) -> Unit  // Callback para eliminar usuario
) {
    val db = FirebaseFirestore.getInstance()
    var users by remember { mutableStateOf(listOf<Map<String, Any>>()) }

    // Carga inicial de datos desde Firebase Firestore
    LaunchedEffect(Unit) {
        try {
            val userDocs = db.collection("users").get().await()
            users = userDocs.documents.map { doc ->
                doc.data?.toMutableMap()?.also { it["userId"] = doc.id } ?: emptyMap()
            }
        } catch (e: Exception) {
            // Manejo de errores
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registered Users", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White)
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (users.isEmpty()) {
                        Text(
                            text = "No users registered",
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    } else {
                        users.forEach { user ->
                            val userId = user["userId"] as? String ?: ""
                            val username = user["username"] as? String ?: "No Name"
                            val email = user["email"] as? String ?: "No Email"

                            if (userId.isNotEmpty()) {
                                UserCard(
                                    username = username,
                                    email = email,
                                    onEditClick = { onEditUserClick(userId) },
                                    onDeleteClick = { onDeleteUserClick(userId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun UserCard(
    username: String,
    email: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onEditClick() },  // La tarjeta es clickeable para editar
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de usuario
            Image(
                painter = painterResource(id = R.drawable.user_placeholder), // Reemplaza con tu recurso de imagen
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = username, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = email, fontSize = 14.sp, color = Color.Gray)
                Text(text = "PLACE OWNER", fontSize = 12.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Botón de editar
            IconButton(onClick = { onEditClick() }) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit User",
                    tint = Color(0xFFFFA500)
                )
            }

            // Botón de eliminar
            IconButton(onClick = { onDeleteClick() }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete User",
                    tint = Color.Red
                )
            }
        }
    }
}

