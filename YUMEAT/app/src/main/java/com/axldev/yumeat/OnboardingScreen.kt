package com.axldev.yumeat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch

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
fun OnboardingScreen(onFinish: () -> Unit) {
    val pagerState = rememberPagerState(initialPage = 0) { pagerItems.size }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDE7))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Reemplazar el texto por la imagen del logo
            Image(
                painter = painterResource(id = R.drawable.applogo),
                contentDescription = "YumEat Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 55.dp),
                contentScale = ContentScale.Fit
            )

            // Contenedor para la imagen, el recuadro naranja y el botón
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                // Recuadro naranja
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .padding(top = 100.dp)
                        .padding(bottom = 1.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFF0AE01), Color(0xFFFF6B00)),
                                start = Offset(0f, 0f),
                                end = Offset(600f, 800f)
                            )
                        )
                ) {
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
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 20.dp, top = 1.dp),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = pagerItems[page].second,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(horizontal = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Imagen superpuesta
                Image(
                    painter = painterResource(id = R.drawable.food_image),
                    contentDescription = "Food Image",
                    modifier = Modifier
                        .size(200.dp)
                        .offset(y = (-20).dp)
                        .align(Alignment.TopCenter),
                    contentScale = ContentScale.Crop
                )

                // Botón "Get Started" solo en la última página
                val isLastPage = pagerState.currentPage == pagerItems.size - 1
                if (isLastPage) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                onFinish()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 200.dp)
                            .height(56.dp)
                            .width(200.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFC700)
                        )
                    ) {
                        Text(text = "Get Started", color = Color.Black)
                    }
                }
            }
        }
    }
}
