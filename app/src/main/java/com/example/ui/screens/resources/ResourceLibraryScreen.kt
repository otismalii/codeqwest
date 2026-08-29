package com.example.ui.screens.resources

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.ResourceLibraryData
import com.example.domain.model.ResourceCategory
import com.example.domain.model.ResourceDocument
import com.example.ui.components.CyberCard
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberGold
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceLight
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceLibraryScreen(
    onSelectDocument: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ResourceCategory?>(null) }

    val allDocs = remember { ResourceLibraryData.getAllDocuments() }

    val filteredDocs = remember(searchQuery, selectedCategory) {
        allDocs.filter { doc ->
            val matchesCategory = selectedCategory == null || doc.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    doc.title.contains(searchQuery, ignoreCase = true) ||
                    doc.subtitle.contains(searchQuery, ignoreCase = true) ||
                    doc.summary.contains(searchQuery, ignoreCase = true) ||
                    doc.geoTag.contains(searchQuery, ignoreCase = true) ||
                    doc.funMemeQuote.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        containerColor = CyberBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.linearGradient(listOf(NeonCyan, CyberPurple))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Cyber Library & Archives",
                                style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Pioneers, Geography & Tech Intuition",
                                style = androidx.compose.material3.MaterialTheme.typography.bodySmall.copy(
                                    color = NeonCyan,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CyberSurface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp)
        ) {
            // SEARCH BAR
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("library_search_input"),
                    placeholder = {
                        Text("Search archives, Grace Hopper, sharks, RAM, memes...", color = TextTertiary, fontSize = 13.sp)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = NeonCyan)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CyberSurface,
                        unfocusedContainerColor = CyberSurface,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = CyberBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            // CATEGORY CHIPS
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text("All Archives (${allDocs.size})") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonCyan,
                                selectedLabelColor = Color.Black,
                                containerColor = CyberSurfaceVariant,
                                labelColor = TextSecondary
                            )
                        )
                    }

                    items(ResourceCategory.values()) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = {
                                selectedCategory = if (selectedCategory == category) null else category
                            },
                            label = { Text(category.title) },
                            leadingIcon = {
                                Icon(
                                    imageVector = getCategoryIcon(category),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonCyan,
                                selectedLabelColor = Color.Black,
                                containerColor = CyberSurfaceVariant,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }
            }

            // FEATURED HERO BANNER (When no search active)
            if (searchQuery.isBlank() && selectedCategory == null) {
                item {
                    FeaturedHeroArchiveCard(
                        doc = allDocs[1], // Undersea Cables
                        onRead = { onSelectDocument(allDocs[1].id) }
                    )
                }
            }

            // SECTION HEADER
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedCategory != null) selectedCategory!!.title else "Curated Archives (${filteredDocs.size})",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Interactive E-Reader",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall.copy(
                            color = NeonEmerald,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            // LIST OF DOCUMENTS
            if (filteredDocs.isEmpty()) {
                item {
                    CyberCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No archives found matching '$searchQuery'", color = TextSecondary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Try searching for 'history', 'Einstein', 'RAM', or 'moth'", color = NeonCyan, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                items(filteredDocs) { doc ->
                    DocumentArchiveCard(
                        doc = doc,
                        onClick = { onSelectDocument(doc.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedHeroArchiveCard(
    doc: ResourceDocument,
    onRead: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onRead() }
            .testTag("featured_archive_hero"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CyberSurface),
        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f))
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Background glow effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(NeonCyan.copy(alpha = 0.15f), Color.Transparent),
                            radius = 400f
                        )
                    )
            )

            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonCyan.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "PLANETARY GEOGRAPHY ARCHIVE",
                            color = NeonCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("${doc.readTimeMinutes} min read", color = TextTertiary, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = doc.title,
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = doc.subtitle,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Fun quote
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(CyberSurfaceVariant)
                        .padding(10.dp)
                ) {
                    Text(
                        text = doc.funMemeQuote,
                        color = CyberGold,
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📍 ${doc.geoTag}",
                        color = TextTertiary,
                        fontSize = 11.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Read Archive",
                            color = NeonCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DocumentArchiveCard(
    doc: ResourceDocument,
    onClick: () -> Unit
) {
    CyberCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("doc_card_${doc.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(getCategoryColor(doc.category).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getCategoryIcon(doc.category),
                            contentDescription = null,
                            tint = getCategoryColor(doc.category),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = doc.category.tag.uppercase(),
                        color = getCategoryColor(doc.category),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Timer, contentDescription = null, tint = TextTertiary, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("${doc.readTimeMinutes} min", color = TextTertiary, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = doc.title,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = doc.summary,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    lineHeight = 16.sp
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = doc.publicationYear,
                    color = TextTertiary,
                    fontSize = 10.sp
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Open E-Reader",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

fun getCategoryIcon(category: ResourceCategory): ImageVector {
    return when (category) {
        ResourceCategory.HISTORY_PIONEERS -> Icons.Default.HistoryEdu
        ResourceCategory.GEOGRAPHY_INFRA -> Icons.Default.Public
        ResourceCategory.EVERYDAY_ANALOGIES -> Icons.Default.Lightbulb
        ResourceCategory.CYBER_SLEUTH -> Icons.Default.Security
        ResourceCategory.ALGORITHMS_LORE -> Icons.Default.Code
    }
}

fun getCategoryColor(category: ResourceCategory): Color {
    return when (category) {
        ResourceCategory.HISTORY_PIONEERS -> CyberPurple
        ResourceCategory.GEOGRAPHY_INFRA -> NeonCyan
        ResourceCategory.EVERYDAY_ANALOGIES -> CyberGold
        ResourceCategory.CYBER_SLEUTH -> NeonEmerald
        ResourceCategory.ALGORITHMS_LORE -> NeonCyan
    }
}
