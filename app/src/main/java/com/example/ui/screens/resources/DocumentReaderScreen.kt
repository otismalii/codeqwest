package com.example.ui.screens.resources

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.ResourceLibraryData
import com.example.domain.model.DocumentSection
import com.example.domain.model.IntuitionQuestion
import com.example.domain.model.ResourceDocument
import com.example.ui.components.CyberCard
import com.example.ui.components.GlowButton
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

enum class ReaderTheme(
    val label: String,
    val bg: Color,
    val surface: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val accent: Color
) {
    CYBER_NIGHT(
        "Cyber Dark",
        CyberBackground,
        CyberSurface,
        TextPrimary,
        TextSecondary,
        NeonCyan
    ),
    PARCHMENT(
        "Parchment",
        Color(0xFFFBF0D9),
        Color(0xFFF3E5C8),
        Color(0xFF2C2216),
        Color(0xFF5C4E3C),
        Color(0xFF8B4513)
    ),
    CLEAN_PAPER(
        "Clean Paper",
        Color(0xFFF8FAFC),
        Color(0xFFFFFFFF),
        Color(0xFF0F172A),
        Color(0xFF475569),
        Color(0xFF0284C7)
    ),
    SOLARIZED_AMBER(
        "Solar Amber",
        Color(0xFF1C1917),
        Color(0xFF292524),
        Color(0xFFFEF3C7),
        Color(0xFFD6D3D1),
        CyberGold
    )
}

enum class ReaderMode {
    E_READER,
    PDF_DOCUMENT_FORMAT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentReaderScreen(
    documentId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val doc = remember(documentId) { ResourceLibraryData.getDocumentById(documentId) }

    if (doc == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CyberBackground),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Document not found", color = TextPrimary)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBack) { Text("Back to Library") }
            }
        }
        return
    }

    var readerMode by remember { mutableStateOf(ReaderMode.E_READER) }
    var currentTheme by remember { mutableStateOf(ReaderTheme.CYBER_NIGHT) }
    var fontSizeMultiplier by remember { mutableFloatStateOf(1.0f) } // 0.85f to 1.35f
    var isBookmarked by remember { mutableStateOf(false) }
    var showFormatControls by remember { mutableStateOf(false) }

    // Intuition Challenge State
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var isAnswerSubmitted by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val scrollProgress by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            if (totalItems <= 1) 0f
            else (listState.firstVisibleItemIndex.toFloat() / (totalItems - 1).toFloat()).coerceIn(0f, 1f)
        }
    }

    val animatedBg by animateColorAsState(currentTheme.bg, label = "bg_anim")
    val animatedTextPrimary by animateColorAsState(currentTheme.textPrimary, label = "text_prim_anim")
    val animatedTextSecondary by animateColorAsState(currentTheme.textSecondary, label = "text_sec_anim")

    Scaffold(
        containerColor = animatedBg,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = doc.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = animatedTextPrimary
                                ),
                                maxLines = 1
                            )
                            Text(
                                text = "${doc.category.tag} • ${doc.readTimeMinutes} min read",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = currentTheme.accent,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("back_button")) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = animatedTextPrimary
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { showFormatControls = !showFormatControls }) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = "Formatting",
                                tint = if (showFormatControls) currentTheme.accent else animatedTextSecondary
                            )
                        }

                        IconButton(
                            onClick = {
                                isBookmarked = !isBookmarked
                                Toast.makeText(
                                    context,
                                    if (isBookmarked) "Saved to Cadet Bookmarks! 🔖" else "Removed from Bookmarks",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (isBookmarked) CyberGold else animatedTextSecondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = currentTheme.surface
                    )
                )

                // Scroll progress indicator
                LinearProgressIndicator(
                    progress = { scrollProgress },
                    modifier = Modifier.fillMaxWidth().height(3.dp),
                    color = currentTheme.accent,
                    trackColor = currentTheme.surface
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // FORMATTING CONTROLS DRAWER / PANEL
            AnimatedVisibility(visible = showFormatControls) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                    border = BorderStroke(1.dp, currentTheme.accent.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // MODE SELECTOR: E-Reader vs PDF / Document Layout
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Layout Format:",
                                color = animatedTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { readerMode = ReaderMode.E_READER },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (readerMode == ReaderMode.E_READER) currentTheme.accent else currentTheme.surface
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    border = BorderStroke(1.dp, currentTheme.accent)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("E-Reader", fontSize = 11.sp, color = if (readerMode == ReaderMode.E_READER) Color.Black else animatedTextPrimary)
                                }

                                Button(
                                    onClick = { readerMode = ReaderMode.PDF_DOCUMENT_FORMAT },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (readerMode == ReaderMode.PDF_DOCUMENT_FORMAT) currentTheme.accent else currentTheme.surface
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    border = BorderStroke(1.dp, currentTheme.accent)
                                ) {
                                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("PDF / Doc", fontSize = 11.sp, color = if (readerMode == ReaderMode.PDF_DOCUMENT_FORMAT) Color.Black else animatedTextPrimary)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // THEME & FONT CONTROLS
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // FONT SIZE BUTTONS
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Size:", color = animatedTextSecondary, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { fontSizeMultiplier = (fontSizeMultiplier - 0.15f).coerceAtLeast(0.85f) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Text("A-", color = animatedTextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                                Text("${(fontSizeMultiplier * 100).toInt()}%", color = currentTheme.accent, fontSize = 11.sp)
                                IconButton(
                                    onClick = { fontSizeMultiplier = (fontSizeMultiplier + 0.15f).coerceAtMost(1.45f) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Text("A+", color = animatedTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }

                            // THEME PICKER
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                ReaderTheme.values().forEach { th ->
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(th.bg)
                                            .border(
                                                width = if (currentTheme == th) 2.dp else 1.dp,
                                                color = if (currentTheme == th) th.accent else CyberBorder,
                                                shape = CircleShape
                                            )
                                            .clickable { currentTheme = th }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // MAIN DOCUMENT BODY
            when (readerMode) {
                ReaderMode.E_READER -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 18.dp),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // HEADER
                        item {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(currentTheme.accent.copy(alpha = 0.18f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = doc.category.title.uppercase(),
                                        color = currentTheme.accent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = doc.title,
                                    color = animatedTextPrimary,
                                    fontSize = 24.sp * fontSizeMultiplier,
                                    fontWeight = FontWeight.ExtraBold,
                                    lineHeight = 30.sp * fontSizeMultiplier
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = doc.subtitle,
                                    color = animatedTextSecondary,
                                    fontSize = 15.sp * fontSizeMultiplier,
                                    fontStyle = FontStyle.Italic
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "By ${doc.authorOrSource}",
                                        color = animatedTextSecondary,
                                        fontSize = 11.sp
                                    )
                                    if (doc.geoTag.isNotBlank()) {
                                        Text(
                                            text = "📍 ${doc.geoTag}",
                                            color = currentTheme.accent,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // MEME QUOTE BOX
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = currentTheme.surface),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, currentTheme.accent.copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "💡",
                                            fontSize = 20.sp
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = doc.funMemeQuote,
                                            color = animatedTextPrimary,
                                            fontSize = 13.sp * fontSizeMultiplier,
                                            fontWeight = FontWeight.Medium,
                                            fontStyle = FontStyle.Italic
                                        )
                                    }
                                }
                            }
                        }

                        // SECTIONS
                        itemsIndexed(doc.sections) { index, section ->
                            DocumentSectionView(
                                section = section,
                                theme = currentTheme,
                                fontSizeMultiplier = fontSizeMultiplier
                            )
                        }

                        // INTUITION CHALLENGE CHECKPOINT
                        item {
                            IntuitionChallengeCard(
                                challenge = doc.intuitionChallenge,
                                theme = currentTheme,
                                fontSizeMultiplier = fontSizeMultiplier,
                                selectedIndex = selectedOptionIndex,
                                isSubmitted = isAnswerSubmitted,
                                onSelectOption = { idx ->
                                    if (!isAnswerSubmitted) selectedOptionIndex = idx
                                },
                                onSubmit = {
                                    isAnswerSubmitted = true
                                    if (selectedOptionIndex == doc.intuitionChallenge.correctIndex) {
                                        Toast.makeText(context, "🌟 Critical Thinking +50 XP Earned!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }

                ReaderMode.PDF_DOCUMENT_FORMAT -> {
                    PdfFormattedDocumentView(
                        doc = doc,
                        theme = currentTheme,
                        fontSizeMultiplier = fontSizeMultiplier,
                        onShare = {
                            Toast.makeText(context, "Simulated PDF Download: '${doc.title}.pdf' saved!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DocumentSectionView(
    section: DocumentSection,
    theme: ReaderTheme,
    fontSizeMultiplier: Float
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = section.heading,
            color = theme.textPrimary,
            fontSize = 18.sp * fontSizeMultiplier,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = section.body,
            color = theme.textSecondary,
            fontSize = 14.sp * fontSizeMultiplier,
            lineHeight = 22.sp * fontSizeMultiplier,
            textAlign = TextAlign.Start
        )

        // Real-world history / geography fact callout
        if (!section.realWorldFact.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = theme.surface),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, theme.accent.copy(alpha = 0.5f))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = theme.accent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "REAL-WORLD ARTIFACT & GEOGRAPHY",
                            color = theme.accent,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = section.realWorldFact,
                            color = theme.textPrimary,
                            fontSize = 12.sp * fontSizeMultiplier,
                            lineHeight = 17.sp * fontSizeMultiplier
                        )
                    }
                }
            }
        }

        // Callout box
        if (!section.calloutBox.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = theme.surface),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, CyberGold.copy(alpha = 0.5f))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = CyberGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = section.calloutBox,
                        color = theme.textPrimary,
                        fontSize = 12.sp * fontSizeMultiplier,
                        lineHeight = 17.sp * fontSizeMultiplier
                    )
                }
            }
        }

        // Meme insight
        if (!section.memeInsight.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(theme.surface)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "🤖 Cyber Meme Lore: ${section.memeInsight}",
                    color = CyberPurple,
                    fontSize = 11.sp * fontSizeMultiplier,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
private fun IntuitionChallengeCard(
    challenge: IntuitionQuestion,
    theme: ReaderTheme,
    fontSizeMultiplier: Float,
    selectedIndex: Int,
    isSubmitted: Boolean,
    onSelectOption: (Int) -> Unit,
    onSubmit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("intuition_challenge_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = theme.surface),
        border = BorderStroke(1.5.dp, theme.accent)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🧠", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TEST YOUR INTUITION",
                        color = theme.accent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(CyberGold.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text("+50 XP REWARD", color = CyberGold, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = challenge.question,
                color = theme.textPrimary,
                fontSize = 15.sp * fontSizeMultiplier,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp * fontSizeMultiplier
            )

            Spacer(modifier = Modifier.height(14.dp))

            // OPTIONS
            challenge.options.forEachIndexed { index, option ->
                val isSelected = selectedIndex == index
                val isCorrect = isSubmitted && index == challenge.correctIndex
                val isWrong = isSubmitted && isSelected && index != challenge.correctIndex

                val borderColor = when {
                    isCorrect -> NeonEmerald
                    isWrong -> Color(0xFFFF3D71)
                    isSelected -> theme.accent
                    else -> theme.surface.copy(alpha = 0.8f)
                }

                val optionBg = when {
                    isCorrect -> NeonEmerald.copy(alpha = 0.15f)
                    isWrong -> Color(0xFFFF3D71).copy(alpha = 0.15f)
                    isSelected -> theme.accent.copy(alpha = 0.12f)
                    else -> theme.bg
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable(enabled = !isSubmitted) { onSelectOption(index) },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = optionBg),
                    border = BorderStroke(1.dp, borderColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isSelected || isCorrect) theme.accent else theme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = ('A' + index).toString(),
                                color = if (isSelected || isCorrect) Color.Black else theme.textSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = option,
                            color = theme.textPrimary,
                            fontSize = 13.sp * fontSizeMultiplier,
                            modifier = Modifier.weight(1f)
                        )

                        if (isCorrect) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = NeonEmerald, modifier = Modifier.size(18.dp))
                        } else if (isWrong) {
                            Icon(Icons.Default.Close, contentDescription = "Wrong", tint = Color(0xFFFF3D71), modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!isSubmitted) {
                Button(
                    onClick = onSubmit,
                    enabled = selectedIndex >= 0,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = theme.accent),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Submit Intuition Check", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            } else {
                // EXPLANATION
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedIndex == challenge.correctIndex) NeonEmerald.copy(alpha = 0.1f) else CyberGold.copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(1.dp, if (selectedIndex == challenge.correctIndex) NeonEmerald else CyberGold)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = if (selectedIndex == challenge.correctIndex) "🎉 Spot-on critical thinking!" else "🧐 Close! Here's the core insight:",
                            color = if (selectedIndex == challenge.correctIndex) NeonEmerald else CyberGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = challenge.explanation,
                            color = theme.textPrimary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                        if (challenge.memeSnark.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "👾 Cadet Memo: ${challenge.memeSnark}",
                                color = CyberPurple,
                                fontSize = 11.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PdfFormattedDocumentView(
    doc: ResourceDocument,
    theme: ReaderTheme,
    fontSizeMultiplier: Float,
    onShare: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 40.dp)
    ) {
        item {
            // PDF SIMULATION TOOLBAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = Color(0xFFFF3D71), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PDF DOCUMENT PREVIEW",
                        color = theme.textPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = onShare,
                    colors = ButtonDefaults.buttonColors(containerColor = theme.accent),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export / Print", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            // FORMAL DOCUMENT SHEET
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFCBD5E1))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // HEADER FOLIO
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "CODEQUEST CYBERNETIC RESEARCH ARCHIVE",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                        Text(
                            text = "DOC ID: ${doc.id.uppercase()}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Color(0xFF0F172A))
                            .padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = doc.title,
                        fontFamily = FontFamily.Serif,
                        fontSize = 20.sp * fontSizeMultiplier,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        lineHeight = 24.sp * fontSizeMultiplier
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = doc.subtitle,
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 13.sp * fontSizeMultiplier,
                        color = Color(0xFF475569)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // METADATA TABLE
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text("Author/Source: ", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color(0xFF334155))
                                Text(doc.authorOrSource, fontSize = 10.sp, color = Color(0xFF334155))
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text("Publication: ", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color(0xFF334155))
                                Text(doc.publicationYear, fontSize = 10.sp, color = Color(0xFF334155))
                            }
                            if (doc.geoTag.isNotBlank()) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Text("Geographic Coordinates: ", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color(0xFF334155))
                                    Text(doc.geoTag, fontSize = 10.sp, color = Color(0xFF334155))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // EXECUTIVE SUMMARY
                    Text(
                        text = "ABSTRACT & INTUITION BRIEF",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0284C7)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = doc.summary,
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.sp * fontSizeMultiplier,
                        lineHeight = 17.sp * fontSizeMultiplier,
                        color = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // SECTIONS
                    doc.sections.forEach { sec ->
                        Text(
                            text = sec.heading,
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp * fontSizeMultiplier,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = sec.body,
                            fontFamily = FontFamily.Serif,
                            fontSize = 12.sp * fontSizeMultiplier,
                            lineHeight = 17.sp * fontSizeMultiplier,
                            color = Color(0xFF334155)
                        )
                        if (!sec.realWorldFact.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE0F2FE))
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "🌐 Field Fact: ${sec.realWorldFact}",
                                    fontSize = 11.sp * fontSizeMultiplier,
                                    color = Color(0xFF0369A1),
                                    fontStyle = FontStyle.Italic
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // WATERMARK & FOOTER
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFCBD5E1))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Page 1 of 1 • CodeQuest Open Knowledge", fontSize = 9.sp, color = Color(0xFF94A3B8))
                        Text("AUTHENTICATED HISTORICAL DATA", fontSize = 9.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
