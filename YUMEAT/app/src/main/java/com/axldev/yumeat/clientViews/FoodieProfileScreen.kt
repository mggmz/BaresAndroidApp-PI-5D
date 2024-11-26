package com.axldev.yumeat.clientViews

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.axldev.yumeat.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FoodieProfileScreen(
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLikesClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onEditInfoClick: () -> Unit
) {
    val backgroundColor = Color(0xFFFFFFFF)
    val buttonChangePasswordColor = Color(0xFFFFBA8F)
    val buttonRecoverPasswordColor = Color(0xFF0072A3)
    val buttonLogoutColor = Color(0xE8EF2D2D)

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
                    androidx.compose.material3.IconButton(
                        onClick = onHomeClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        androidx.compose.material3.Icon(
                            Icons.Filled.Home,
                            contentDescription = "Home",
                            tint = Color.Gray
                        )
                    }
                    androidx.compose.material3.IconButton(
                        onClick = onOffersClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        androidx.compose.material3.Icon(
                            Icons.Filled.LocalOffer,
                            contentDescription = "Offers",
                            tint = Color.Gray
                        )
                    }
                    androidx.compose.material3.IconButton(
                        onClick = onLikesClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        androidx.compose.material3.Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Likes",
                            tint = Color.Gray
                        )
                    }
                    androidx.compose.material3.IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier.size(48.dp)
                    ) {
                        androidx.compose.material3.Icon(
                            Icons.Filled.Person,
                            contentDescription = "Profile",
                            tint = Color.Gray
                        )
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
        backgroundColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de fondo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder_image2),
                    contentDescription = "Foodie Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Imagen de perfil
            Box(
                modifier = Modifier
                    .offset(y = (-50).dp)
                    .size(140.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_placeholder2),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(2.dp, backgroundColor, CircleShape)
                )
            }

            // Nombre y descripción
            Text(
                text = "Username",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
            Text(
                text = "Foodie",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sección de información
            Text(
                text = "Información",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 40.dp),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Username",
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = "fernandak@gmail.com",
                fontSize = 16.sp,
                color = Color.Gray
            )

            // Botón para editar información
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onEditInfoClick,
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonRecoverPasswordColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Editar Información",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de acción (Contraseña)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de cerrar sesión
            Button(
                onClick = onLogoutClick,
                colors = ButtonDefaults.buttonColors(backgroundColor = buttonLogoutColor),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "Cerrar Sesión",
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FoodieProfileScreenPreview() {
    FoodieProfileScreen(
        onHomeClick = {},
        onOffersClick = {},
        onProfileClick = {},
        onLikesClick = {}, // Agregado para el preview
        onLogoutClick = {}, // Agregado para el preview
        onEditInfoClick = {}
    )
}
