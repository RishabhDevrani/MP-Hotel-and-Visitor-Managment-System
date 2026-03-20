package com.example.guestservicesapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private data class FoodItem(
    val name: String,
    val description: String,
    val price: String,
    val category: String,
    val icon: ImageVector,
    val color: Color,
    val isVeg: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen() {
    var showBuffet by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()
    val gridState = rememberLazyGridState()

    LaunchedEffect(Unit) {
        visible = true
    }

    val categories = listOf("All", "Starters", "Main Course", "Chinese", "Desserts", "Beverages")

    val menuItems = listOf(
        FoodItem("Cleansing Green", "Apple, cucumber, spinach & lemon", "₹180", "Beverages", Icons.Default.LocalDrink, Color(0xFFE8F5E9)),
        FoodItem("Leafy Purple", "Cucumber, kale & lemon", "₹195", "Beverages", Icons.Default.LocalDrink, Color(0xFFF3E5F5)),
        FoodItem("Energy Boost", "Carrot, orange & apple", "₹170", "Beverages", Icons.Default.LocalDrink, Color(0xFFFFF3E0)),
        FoodItem("Warrior One", "Apple, beetroot & ginger", "₹210", "Beverages", Icons.Default.LocalDrink, Color(0xFFFFEBEE)),
        FoodItem("Paneer Tikka", "Grilled cottage cheese with spices", "₹320", "Starters", Icons.Default.Restaurant, Color(0xFFFFF9C4)),
        FoodItem("Chicken Biryani", "Aromatic basmati rice with chicken", "₹480", "Main Course", Icons.Default.Restaurant, Color(0xFFFFE0B2), false),
        FoodItem("Spring Rolls", "Crispy veg rolls with sweet chili", "₹240", "Chinese", Icons.Default.Fastfood, Color(0xFFDCEDC8)),
        FoodItem("Hakka Noodles", "Stir-fried noodles with veggies", "₹290", "Chinese", Icons.Default.Fastfood, Color(0xFFF0F4C3)),
        FoodItem("Gulab Jamun", "Milk solids balls in sugar syrup", "₹140", "Desserts", Icons.Default.Icecream, Color(0xFFFFCCBC)),
        FoodItem("Choco Lava", "Warm chocolate cake with lava center", "₹220", "Desserts", Icons.Default.Icecream, Color(0xFFD7CCC8)),
        FoodItem("Veg Manchurian", "Deep fried veg balls in spicy gravy", "₹280", "Chinese", Icons.Default.Fastfood, Color(0xFFE1F5FE)),
        FoodItem("Butter Chicken", "Rich creamy tomato chicken gravy", "₹520", "Main Course", Icons.Default.Restaurant, Color(0xFFFFEBEE), false)
    )

    val filteredItems = menuItems.filter {
        (selectedCategory == "All" || it.category == selectedCategory) &&
        (it.name.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true))
    }

    // Parallax logic: Calculate offset of the first item
    val headerTranslationY = if (gridState.firstVisibleItemIndex == 0) {
        -gridState.firstVisibleItemScrollOffset.toFloat() * 0.4f
    } else {
        0f
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        /* 1. Header & Search Bar - Spans full width */
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -40 })
            ) {
                Column(
                    modifier = Modifier
                        .graphicsLayer { translationY = headerTranslationY }
                        .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 12.dp)
                ) {
                    Text(
                        text = "Discovery",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "The perfect Healthy Meals",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)),
                        placeholder = { Text("Search items...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            unfocusedBorderColor = Color.Transparent,
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                }
            }
        }

        /* 2. Menu / Buffet Toggle - Spans full width */
        item(span = { GridItemSpan(maxCurrentLineSpan) }) {
            AnimatedVisibility(visible = visible, enter = fadeIn(animationSpec = tween(600))) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (!showBuffet) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent)
                            .clickable { showBuffet = false },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "A La Carte",
                            fontWeight = FontWeight.Bold,
                            color = if (!showBuffet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (showBuffet) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent)
                            .clickable { showBuffet = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Buffet System",
                            fontWeight = FontWeight.Bold,
                            color = if (showBuffet) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        /* 3. Conditional Content */
        if (showBuffet) {
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                Column(Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                    Text("Active Buffet Plans", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    Spacer(Modifier.height(16.dp))
                    BuffetPlanCard("Breakfast Buffet", "07:30 AM - 10:30 AM", "₹399 per person", Color(0xFFFFA62B))
                    Spacer(Modifier.height(16.dp))
                    BuffetPlanCard("Grand Lunch Buffet", "12:30 PM - 03:30 PM", "₹749 per person", Color(0xFF6C63FF))
                    Spacer(Modifier.height(16.dp))
                    BuffetPlanCard("Dinner Buffet", "07:30 PM - 10:30 PM", "₹899 per person", Color(0xFF2EC4B6))
                }
            }
        } else {
            /* 3a. Categories Row */
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                AnimatedVisibility(visible = visible, enter = slideInHorizontally { -100 } + fadeIn()) {
                    LazyRow(
                        modifier = Modifier.padding(vertical = 12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { category ->
                            FilterChip(
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category },
                                label = { Text(category) },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }
            }

            /* 3b. "Our top picks" Title */
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                Text(
                    text = if (searchQuery.isEmpty()) "Our top picks" else "Search results",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            /* 3c. The Actual Grid of Food */
            if (filteredItems.isEmpty()) {
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        Text("No matches found", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                    }
                }
            } else {
                itemsIndexed(filteredItems) { index, item ->
                    val itemVisible = remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(index * 40L)
                        itemVisible.value = true
                    }
                    AnimatedVisibility(
                        visible = itemVisible.value,
                        enter = scaleIn(initialScale = 0.85f) + fadeIn(),
                        modifier = Modifier.padding(
                            start = if (index % 2 == 0) 20.dp else 8.dp,
                            end = if (index % 2 == 0) 8.dp else 20.dp,
                            bottom = 16.dp
                        )
                    ) {
                        NewFoodCard(item)
                    }
                }
            }
        }
    }
}

@Composable
private fun NewFoodCard(item: FoodItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(item.color.copy(alpha = if (isSystemInDarkTheme()) 0.2f else 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = if (isSystemInDarkTheme()) item.color else Color.Black.copy(alpha = 0.25f)
                )
                Box(Modifier.fillMaxSize().padding(8.dp), contentAlignment = Alignment.TopEnd) {
                    Box(
                        Modifier
                            .size(12.dp)
                            .border(1.dp, if (item.isVeg) Color(0xFF4CAF50) else Color(0xFFD32F2F))
                            .padding(2.dp)
                    ) {
                        Box(Modifier.fillMaxSize().background(if (item.isVeg) Color(0xFF4CAF50) else Color(0xFFD32F2F), CircleShape))
                    }
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            Text(
                text = item.name,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                maxLines = 2,
                minLines = 2,
                lineHeight = 14.sp
            )
            
            Spacer(Modifier.height(8.dp))
            
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.price,
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = Color(0xFF8BC34A).copy(alpha = 0.15f)
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color(0xFF558B2F),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BuffetPlanCard(title: String, time: String, price: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = color.copy(alpha = 0.15f), modifier = Modifier.size(40.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.RestaurantMenu, contentDescription = null, tint = color)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Text(title, fontWeight = FontWeight.Black, fontSize = 19.sp, color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Column {
                    Text(time, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 13.sp)
                    Text(price, fontWeight = FontWeight.Black, fontSize = 18.sp, color = color)
                }
                Button(
                    onClick = { },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = color)
                ) {
                    Text("Book Now")
                }
            }
        }
    }
}
