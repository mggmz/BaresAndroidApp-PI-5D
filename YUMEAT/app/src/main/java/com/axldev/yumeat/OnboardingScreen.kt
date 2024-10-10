package com.axldev.yumeat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch

// Crear FontFamily para Quicksand y Raleway
val quicksandFont = FontFamily(Font(R.font.quicksand))
val ralewayFont = FontFamily(Font(R.font.raleway))

@Composable
fun OnboardingScreen() {
    // List of items for the pager
    val pagerItems = listOf(
        "Locate the best food",
        "Discover great deals",
        "Enjoy the best restaurants"
    )

    // Recordar el estado de la lista para LazyRow
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDE7)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Title and Subtitle
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

        // Static box for the orange rectangle
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
            // Horizontal Pager inside the orange rectangle
            LazyRow(
                state = lazyListState, // Agrega el estado de la lista
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp) // Margen considerable del texto
            ) {
                items(pagerItems) { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = item,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Discover the best restaurants, bars, and food spots near you.",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Get Started Button
        Button(
            onClick = {
                // Controlar el deslizamiento del pager con coroutines para cambiar de página
                coroutineScope.launch {
                    val nextPage = (lazyListState.firstVisibleItemIndex + 1).coerceAtMost(pagerItems.size - 1)
                    lazyListState.animateScrollToItem(nextPage)
                }
            },
            modifier = Modifier
                .padding(top = 40.dp)
                .height(60.dp)
                .width(180.dp)
        ) {
            Text(
                text = "Get Started!!",
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
