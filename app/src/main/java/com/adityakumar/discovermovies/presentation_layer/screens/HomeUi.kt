package com.adityakumar.discovermovies.presentation_layer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.adityakumar.discovermovies.domain_layer.dataModel.Result
import com.adityakumar.discovermovies.presentation_layer.viewModel.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUi(
    onMovieClick: (Int) -> Unit,
    viewModel: MovieViewModel = hiltViewModel()
) {
    val movieState by viewModel.mState.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Rich & Premium Dark Background
    val backgroundColor = Color(0xFF121620)
    val surfaceColor = Color(0xFF1E232E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // --- Clean Top Bar (Only Title) ---
        CenterAlignedTopAppBar(
            title = { 
                Text(
                    text = "Discover Movies",
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF0AF3D4),
                                Color(0xFFE0166B),
                                Color(0xFF76E74E),
                                Color(0xFF4168E5)
                            )
                        )
                    ),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = backgroundColor
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // --- Search Bar with Filter and Clear Button ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = query,
                    onValueChange = {
                        query = it
                        viewModel.searchMovies(it)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    placeholder = { Text("Search for movies...", color = Color.LightGray, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = {
                                query = ""
                                viewModel.searchMovies("")
                                focusManager.clearFocus() // Keyboard aur Cursor hide karne ke liye
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Color.LightGray)
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = surfaceColor,
                        unfocusedContainerColor = surfaceColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .size(54.dp)
                        .background(surfaceColor, RoundedCornerShape(16.dp))
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filter", tint = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- Content Grid ---
            Box(modifier = Modifier.fillMaxSize()) {
                if (movieState.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center), 
                        color = Color(0xFF8AB4F8)
                    )
                }

                if (movieState.error.isNotEmpty()) {
                    Text(
                        text = movieState.error,
                        color = Color(0xFFFF8585),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                movieState.data?.results?.let { movies ->
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        item(span = { GridItemSpan(3) }) {
                            Text(
                                text = if (query.isEmpty()) "Popular Movies" else "Search Results",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        items(movies) { movie ->
                            MovieItem(
                                movie = movie,
                                onClick = { 
                                    focusManager.clearFocus() // Click karte waqt bhi keyboard hide ho jaye
                                    movie.id?.let { onMovieClick(it) } 
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Result, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = if (movie.posterPath != null) "https://image.tmdb.org/t/p/w500${movie.posterPath}" else null,
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.67f)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = movie.title ?: "Unknown",
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = movie.releaseDate?.take(4) ?: "N/A",
            color = Color.LightGray,
            fontSize = 11.sp
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "%.1f".format(java.util.Locale.US, movie.voteAverage ?: 0.0),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
