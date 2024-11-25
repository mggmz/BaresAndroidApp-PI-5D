package com.axldev.yumeat.clientViews

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.Scaffold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.axldev.yumeat.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FoodieProfileScreen(
    onHomeClick: () -> Unit,
    onOffersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onEditInfoClick: () -> Unit
) {
    val backgroundColor = Color(0xFFFFFFFF)
    val buttonChangePasswordColor = Color(0xFFFFBA8F)
    val buttonRecoverPasswordColor = Color(0xFF0072A3)
    val buttonLogoutColor = Color(0xFF2C2C2C)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = onHomeClick,
                onOffersClick = onOffersClick,
                onProfileClick = onProfileClick
            )
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
            //Imagen de fondo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder_image), //Imagen Dummy
                    contentDescription = "Foodie Background Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = { /* Acción para editar fondo */ },
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp)
                        .background(
                            color = Color(0xFFE5F9FF),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar fondo",
                        tint = Color.Black
                    )
                }
            }

            //Imagen de perfil
            Box(
                modifier = Modifier
                    .offset(y = (-50).dp)
                    .size(140.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_placeholder2), //Reemplaza con la imagen real
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(2.dp, backgroundColor, CircleShape)
                )
                IconButton(
                    onClick = { /* Acción para cambiar foto de perfil */ },
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = Color(0xFFE5F9FF),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar foto de perfil",
                        tint = Color.Black
                    )
                }
            }

            //Nombre y descripción
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

            //Sección de información
            Text(
                text = "Información",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black
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

            //Botón para editar información
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

            //Botones de acción (Contraseña)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* Acción para cambiar contraseña */ },
                    colors = ButtonDefaults.buttonColors(backgroundColor = buttonChangePasswordColor),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .height(57.dp),
                    contentPadding = PaddingValues(vertical = 12.dp) //Centrar texto verticalmente
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Cambiar\nContraseña",
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Botón de cerrar sesión
            Button(
                onClick = { /* Acción para cerrar sesión */ },
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
        onEditInfoClick = {}
    )
}

