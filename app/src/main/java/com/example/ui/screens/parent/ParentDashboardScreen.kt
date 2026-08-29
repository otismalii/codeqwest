package com.example.ui.screens.parent

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ActivityAttemptEntity
import com.example.data.local.CadetProfileEntity
import com.example.data.local.ConceptMasteryEntity
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberProgressBar
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberGold
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceLight
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentDashboardScreen(
    profile: CadetProfileEntity,
    attempts: List<ActivityAttemptEntity>,
    masteries: List<ConceptMasteryEntity>,
    onBack: () -> Unit
) {
    val totalAttempts = attempts.size
    val successfulAttempts = attempts.count { it.isSuccess }
    val accuracy = if (totalAttempts > 0) (successfulAttempts.toFloat() / totalAttempts.toFloat() * 100).toInt() else 100
    val totalTimeMinutes = (attempts.sumOf { it.timeSpentMs } / 60000L).coerceAtLeast(1)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = NeonCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardian & Educator Insights", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("parent_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberBackground)
            )
        },
        containerColor = CyberBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Security & Privacy Verification Card
            item {
                CyberCard(borderColor = NeonEmerald) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(NeonEmerald.copy(alpha = 0.15f))
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("100% Local On-Device Learning", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                            Text("Zero cloud tracking, COPPA-compliant encrypted storage.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                }
            }

            // Key Metrics Summary
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricMiniCard(
                        title = "Accuracy Rate",
                        value = "$accuracy%",
                        icon = Icons.Default.TrendingUp,
                        color = NeonEmerald,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        title = "Missions Run",
                        value = "$totalAttempts",
                        icon = Icons.Default.Psychology,
                        color = NeonCyan,
                        modifier = Modifier.weight(1f)
                    )
                    MetricMiniCard(
                        title = "Focus Time",
                        value = "${totalTimeMinutes}m",
                        icon = Icons.Default.Timer,
                        color = CyberGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Concept Competencies Breakdown
            item {
                Text(
                    text = "CURRICULUM COMPETENCY MAP",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = NeonCyan
                )
            }

            item {
                CyberCard(borderColor = CyberBorder) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        CompetencyRow("Computer Basics (I/O, Passwords, Privacy)", calculateCategoryMastery(masteries, "basics"))
                        CompetencyRow("Hardware Lab (Motherboard, CPU/RAM, Binary)", calculateCategoryMastery(masteries, "hardware"))
                        CompetencyRow("Software World (OS, Glitches, Phishing)", calculateCategoryMastery(masteries, "software"))
                        CompetencyRow("Code Academy (Sequences, Loops, Debugging)", calculateCategoryMastery(masteries, "code"))
                    }
                }
            }

            // Offline Family Conversation Starters
            item {
                Text(
                    text = "OFFLINE FAMILY DISCUSSION TOPICS",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                    color = CyberGold
                )
            }

            item {
                CyberCard(borderColor = CyberGold) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        DiscussionPrompt(
                            topic = "Password Passphrases",
                            prompt = "Ask: 'What makes a strong password in CodeQuest?' Practice making a 3-word passphrase together."
                        )
                        DiscussionPrompt(
                            topic = "Phishing & Fake Messages",
                            prompt = "Discuss why urgency (like 'Claim free prize in 5 min!') is a classic scam red flag."
                        )
                        DiscussionPrompt(
                            topic = "Algorithms Around the House",
                            prompt = "Explore real-world loops, like brushing teeth (Repeat 2 minutes) or following a cooking recipe."
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun MetricMiniCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

@Composable
private fun CompetencyRow(label: String, score: Float) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Text("${(score * 100).toInt()}%", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), color = NeonEmerald)
        }
        Spacer(modifier = Modifier.height(6.dp))
        CyberProgressBar(progress = score, color = NeonEmerald, height = 8.dp)
    }
}

@Composable
private fun DiscussionPrompt(topic: String, prompt: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = CyberGold, modifier = Modifier.size(20.dp).padding(top = 2.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(topic, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Text(prompt, style = MaterialTheme.typography.bodySmall, color = TextSecondary, lineHeight = 18.sp)
        }
    }
}

private fun calculateCategoryMastery(masteries: List<ConceptMasteryEntity>, prefix: String): Float {
    val categoryMasteries = masteries.filter { it.conceptId.startsWith(prefix) }
    if (categoryMasteries.isEmpty()) return 0.50f
    return (categoryMasteries.sumOf { it.masteryScore.toDouble() } / categoryMasteries.size).toFloat().coerceIn(0f, 1f)
}
