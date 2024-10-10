package com.axldev.yumeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Crear FontFamily para Quicksand y Raleway
val quicksandFont = FontFamily(Font(R.font.quicksand))
val ralewayFont = FontFamily(Font(R.font.raleway))

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnboardingScreen()
        }
    }
}

@Composable
fun OnboardingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDE7)), // Color de fondo principal
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top // Mueve todo hacia la parte superior
    ) {
        // Título con fuente Quicksand
        Text(
            text = "YumEat",
            fontSize = 50.sp, // Hacemos el texto más grande
            fontWeight = FontWeight.Bold,
            fontFamily = quicksandFont, // Asigna la fuente Quicksand
            modifier = Modifier.padding(top = 40.dp) // Espacio desde arriba
        )

        // Subtítulo con fuente Raleway
        Text(
            text = "Get some food!!",
            fontSize = 20.sp, // Un poco más grande para ser más legible
            modifier = Modifier.padding(top = 4.dp), // Menor espacio entre título y subtítulo
            fontFamily = ralewayFont // Asigna la fuente Raleway
        )

        // Imagen en la parte superior con rectángulo ajustado
        Box(
            modifier = Modifier
                .padding(top = 40.dp)
                .clip(RoundedCornerShape(30.dp)) // Más border-radius
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFA726), Color(0xFFFFC107))
                    )
                )
                .size(300.dp, 400.dp),
            contentAlignment = Alignment.Center
        ) {
            // Texto dentro del rectángulo naranja
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 60.dp)
            ) {
                Text(
                    text = "Locate the best",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "food in your area",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Discover the best restaurants, bars, and food spots near you with the best deals and special offers.",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center // Centramos el texto
                )
            }
        }

        // Botón "Get Started"
        Button(
            onClick = { /* Acción de comenzar */ },
            modifier = Modifier
                .padding(top = 40.dp)
                .height(60.dp) // Más alto
                .width(180.dp), // Menos ancho
            shape = RoundedCornerShape(10.dp), // Menor border-radius
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFFC700) // Color del botón
            )
        ) {
            Text(
                text = "Get Started!!",
                color = Color.Black // Texto en negro
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen()
}
