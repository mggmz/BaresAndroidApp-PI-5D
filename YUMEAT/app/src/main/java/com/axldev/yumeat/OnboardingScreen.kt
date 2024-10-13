package com.axldev.yumeat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource


// Crear FontFamily para Quicksand y Raleway
val quicksandFont = FontFamily(Font(R.font.quicksand))
val ralewayFont = FontFamily(Font(R.font.raleway))

private val pagerItems = listOf(
    "Locate the best food" to "Discover the best restaurants, bars, and food spots near you with great deals and offers.",
    "Discover great deals" to "Find exclusive promotions for your favorite places to eat and drink.",
    "Enjoy the best restaurants" to "Experience the top-rated spots with the best food and service."
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen() {
    val pagerState = rememberPagerState(initialPage = 0) { pagerItems.size }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDE7)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Título y subtítulo
        Text(
            text = "YumEat",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = quicksandFont,
            modifier = Modifier.padding(top = 40.dp)
        )
        Text(
            text = "Get some food!!",
            fontSize = 20.sp,
            fontFamily = ralewayFont,
            modifier = Modifier.padding(top = 4.dp)
        )

        Box(
            modifier = Modifier
                .size(200.dp) // Aumentamos el tamaño del Box
        ) {
            Image(
                painter = painterResource(id = R.drawable.food_image), // Reemplaza con tu recurso de imagen
                contentDescription = "Food Image",
                modifier = Modifier
                    .fillMaxSize(), // Asegura que la imagen llene todo el espacio del Box
                contentScale = ContentScale.Crop // Esto ajusta la imagen para que llene el Box
            )
        }


        // Caja estática para el rectángulo naranja
        Box(
            modifier = Modifier

                .clip(RoundedCornerShape(30.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFF0AE01), Color(0xFFFF6B00)),
                        start = Offset(0f, 0f),
                        end = Offset(600f, 800f)
                    )
                )
                .size(350.dp, 500.dp),
            contentAlignment = Alignment.Center
        ) {
            // Implementación de HorizontalPager para el efecto de deslizar
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = pagerItems[page].first,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = pagerItems[page].second,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Botones de texto "Previous", "Get Started", y "Next" fuera del rectángulo naranja
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 32.dp, end = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Previous solo si no estamos en la primera página
            PreviousButton(pagerState, coroutineScope)

            Spacer(modifier = Modifier.weight(1f)) // Espacio flexible para alinear los botones

            // Botón "Get Started" en el centro
            if (pagerState.currentPage == pagerItems.size - 1) {
                Button(
                    onClick = { /* Acción del botón Get Started */ },
                    modifier = Modifier
                        .height(60.dp)
                        .width(180.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC700) // Color amarillo del botón
                    )
                ) {
                    Text(text = "Get Started!!", color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Espacio flexible

            // Botón Next solo si no estamos en la última página
            if (pagerState.currentPage < pagerItems.size - 1) {
                Text(
                    text = "Next >>",
                    color = Color.Black,
                    modifier = Modifier.clickable {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PreviousButton(pagerState: PagerState, coroutineScope: CoroutineScope) {
    if (pagerState.currentPage > 0) {
        Text(
            text = "<< Previous",
            color = Color.Black,
            modifier = Modifier.clickable {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen()
}
