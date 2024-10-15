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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
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
fun OnboardingScreen() {
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
            // Título y subtítulo
            Text(
                text = "YumEat",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = quicksandFont,
                modifier = Modifier.padding(top = 40.dp)
            )
            Text(
                text = "Get some food!",
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic,
                fontFamily = ralewayFont,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Contenedor para la imagen, el recuadro naranja y el botón
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 20.dp)
            ) {
                // Recuadro naranja
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 100.dp, start = 16.dp, end = 16.dp)
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

                // Imagen superpuesta
                Image(
                    painter = painterResource(id = R.drawable.food_image),
                    contentDescription = "Food Image",
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.TopCenter),
                    contentScale = ContentScale.Crop
                )

                // Botón superpuesto
                val isLastPage = pagerState.currentPage == pagerItems.size - 1
                val buttonText = if (isLastPage) "Get Started" else "Next"

                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (isLastPage) {
                                // Acciones al presionar "Get Started", como cambiar de pantalla
                            } else {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 30.dp)
                        .height(60.dp)
                        .width(280.dp),
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFC700)
                    )
                ) {
                    Text(text = buttonText, color = Color.Black)
                }
            }
        }
    }
}