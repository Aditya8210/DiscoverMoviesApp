package com.adityakumar.discovermovies.presentation_layer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.adityakumar.discovermovies.presentation_layer.viewModel.DetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsUi(
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val state by viewModel.detailsState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFF0F1113)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (state.loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color.Magenta)
            }

            if (state.error.isNotEmpty()) {
                Text(state.error, color = Color.Red, modifier = Modifier.align(Alignment.Center))
            }

            state.data?.let { movie ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Backdrop & Poster
                    Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w780${movie.backdropPath}",
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        
                        // Floating Action Button for Watchlist
                        IconButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp)
                                .size(56.dp)
                                .background(Color(0xFF673AB7), CircleShape)
                        ) {
                            Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = Color.White)
                        }
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = movie.title ?: "Unknown",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = movie.releaseDate?.take(4) ?: "N/A", color = Color.Gray)
                            Text(text = "  •  ", color = Color.Gray)
                            Text(text = "${movie.runtime ?: 0}m", color = Color.Gray)
                            Text(text = "  •  ", color = Color.Gray)
                            Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                            Text(text = " ${String.format("%.1f", movie.voteAverage ?: 0.0)}/10", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Genres
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            movie.genres?.forEach { genre ->
                                Surface(
                                    color = Color(0xFF1C1F22),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = genre.name ?: "",
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(text = "Overview", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = movie.overview ?: "No overview available.",
                            color = Color.Gray,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Button(
                            onClick = { /* TODO */ },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                        ) {
                            Icon(Icons.Default.BookmarkBorder, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Add to Watchlist")
                        }
                    }
                }
            }
        }
    }
}
