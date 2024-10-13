package com.axldev.yumeat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

// Crear FontFamily para Quicksand y Raleway
val quicksandFont = FontFamily(Font(R.font.quicksand))
val ralewayFont = FontFamily(Font(R.font.raleway))

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen() {
    // Lista de elementos para el pager
    val pagerItems = listOf(
        "Locate the best food" to "Discover the best restaurants, bars, and food spots near you with great deals and offers.",
        "Discover great deals" to "Find exclusive promotions for your favorite places to eat and drink.",
        "Enjoy the best restaurants" to "Experience the top-rated spots with the best food and service."
    )

    // Recordar el estado del pager
    val pagerState = rememberPagerState()
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

        // Caja estática para el rectángulo naranja
        Box(
            modifier = Modifier
                .padding(top = 40.dp)
                .clip(RoundedCornerShape(30.dp)) // Border radius ajustado
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFA726), Color(0xFFFFC107)) // Degradado de color
                    )
                )
                .size(300.dp, 400.dp), // Tamaño del box
            contentAlignment = Alignment.Center // Centrar el contenido
        ) {
            // Implementación de HorizontalPager para el efecto de deslizar
            HorizontalPager(
                count = pagerItems.size,
                state = pagerState, // Controla el estado del pager
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = pagerItems[page].first, // Título dinámico
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = pagerItems[page].second, // Descripción dinámica
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Botón "Next" para deslizar al siguiente contenido
        Button(
            onClick = {
                coroutineScope.launch {
                    val nextPage = (pagerState.currentPage + 1).coerceAtMost(pagerItems.size - 1)
                    pagerState.animateScrollToPage(nextPage)
                }
            },
            modifier = Modifier
                .padding(top = 40.dp)
                .height(60.dp)
                .width(180.dp)
        ) {
            Text(
                text = "Next", // Texto del botón
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen()
}
