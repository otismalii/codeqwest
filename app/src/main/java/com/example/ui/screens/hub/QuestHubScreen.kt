package com.example.ui.screens.hub

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ActivityAttemptEntity
import com.example.data.local.ActivityEntity
import com.example.data.local.CadetProfileEntity
import com.example.data.local.ConceptMasteryEntity
import com.example.data.local.CurriculumModuleEntity
import com.example.domain.model.AdaptiveRecommendation
import com.example.domain.model.ModuleCategory
import com.example.domain.model.RecommendationRationale
import com.example.ui.components.CadetAvatarView
import com.example.ui.components.CyberCard
import com.example.ui.components.CyberProgressBar
import com.example.ui.components.GlowButton
import com.example.ui.components.StarRatingRow
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberCrimson
import com.example.ui.theme.CyberGold
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
fun QuestHubScreen(
    profile: CadetProfileEntity,
    modules: List<CurriculumModuleEntity>,
    activities: List<ActivityEntity>,
    attempts: List<ActivityAttemptEntity>,
    masteries: List<ConceptMasteryEntity>,
    recommendation: AdaptiveRecommendation?,
    onLaunchActivity: (String) -> Unit,
    onOpenBadges: () -> Unit,
    onOpenResources: () -> Unit,
    onOpenParentDashboard: () -> Unit
) {
    var expandedModuleId by remember { mutableStateOf<String?>(null) }
    val completedActivityIds = attempts.filter { it.isSuccess }.map { it.activityId }.toSet()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Cadet Profile Chip
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CadetAvatarView(avatarId = profile.avatarId, size = 40.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = profile.callSign,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = "LVL ${profile.level} CADET",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = NeonCyan
                                )
                            }
                        }

                        // Action Icons (Resources, Badges, Parent Dashboard)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onOpenResources, modifier = Modifier.testTag("resources_button")) {
                                Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Library & Archives", tint = NeonCyan)
                            }

                            IconButton(onClick = onOpenBadges, modifier = Modifier.testTag("badge_vault_button")) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = "Badges", tint = CyberGold)
                            }

                            IconButton(onClick = onOpenParentDashboard, modifier = Modifier.testTag("parent_dashboard_button")) {
                                Icon(Icons.Default.FamilyRestroom, contentDescription = "Parent Dashboard", tint = NeonEmerald)
                            }
                        }
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
            // QUICK ACTION NAV BAR (Quests, Library, Parent Hub)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Resources / E-Reader Card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onOpenResources() }
                            .testTag("resources_quick_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberSurface),
                        border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(NeonCyan.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Library & Archives", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text("E-Reader & Memes", color = NeonCyan, fontSize = 10.sp)
                            }
                        }
                    }

                    // Parent Dashboard Card (No Auth)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onOpenParentDashboard() }
                            .testTag("parent_quick_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = CyberSurface),
                        border = BorderStroke(1.dp, NeonEmerald.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(NeonEmerald.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text("Guardian Intel", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text("Direct Dashboard", color = NeonEmerald, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            // Adaptive Mission Recommendation (Deterministic LDX Engine)
            item {
                if (recommendation != null) {
                    AdaptiveQuestCard(
                        recommendation = recommendation,
                        onLaunch = { onLaunchActivity(recommendation.targetActivity.id) }
                    )
                }
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXPLORATION TRACKS",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                        color = NeonCyan
                    )
                    Text(
                        text = "${completedActivityIds.size}/${activities.size} COMPLETE",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = NeonEmerald
                    )
                }
            }

            // 4 Module Track Cards
            items(modules) { module ->
                val moduleActivities = activities.filter { it.moduleId == module.id }
                val completedCount = moduleActivities.count { completedActivityIds.contains(it.id) }
                val isExpanded = expandedModuleId == module.id

                ModuleTrackCard(
                    module = module,
                    activities = moduleActivities,
                    completedActivityIds = completedActivityIds,
                    isExpanded = isExpanded,
                    onToggleExpand = {
                        expandedModuleId = if (isExpanded) null else module.id
                    },
                    onLaunchActivity = onLaunchActivity
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun AdaptiveQuestCard(
    recommendation: AdaptiveRecommendation,
    onLaunch: () -> Unit
) {
    val target = recommendation.targetActivity

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .border(2.dp, NeonEmerald, RoundedCornerShape(22.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberSurface)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Explainable Reason Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonEmerald.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonEmerald, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = recommendation.rationale.tag.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = NeonEmerald
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("⚡ +${target.xpReward} XP", color = CyberGold, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = target.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )

            Text(
                text = recommendation.rationale.explanation,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            GlowButton(
                text = "START ADAPTIVE QUEST ➔",
                onClick = onLaunch,
                accentColor = NeonEmerald,
                icon = Icons.Default.PlayArrow,
                testTag = "start_adaptive_quest_button"
            )
        }
    }
}

@Composable
private fun ModuleTrackCard(
    module: CurriculumModuleEntity,
    activities: List<ActivityEntity>,
    completedActivityIds: Set<String>,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onLaunchActivity: (String) -> Unit
) {
    val completedCount = activities.count { completedActivityIds.contains(it.id) }
    val progress = if (activities.isNotEmpty()) completedCount.toFloat() / activities.size.toFloat() else 0f

    val (bannerRes, iconEmoji) = when (module.category) {
        ModuleCategory.BASICS -> Pair(R.drawable.ic_quest_app_icon, "💻")
        ModuleCategory.HARDWARE -> Pair(R.drawable.ic_hardware_lab, "⚡")
        ModuleCategory.SOFTWARE -> Pair(R.drawable.ic_cadet_mascot, "🌐")
        ModuleCategory.CODE -> Pair(R.drawable.ic_code_academy, "🚀")
    }

    CyberCard(
        borderColor = if (progress >= 1f) NeonEmerald else CyberBorder,
        onClick = onToggleExpand
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(CyberSurfaceVariant)
                        .border(1.dp, NeonCyan, RoundedCornerShape(14.dp))
                ) {
                    Text(iconEmoji, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "${completedCount}/${activities.size} Missions Completed",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (progress >= 1f) NeonEmerald else TextSecondary
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            CyberProgressBar(progress = progress, color = if (progress >= 1f) NeonEmerald else NeonCyan)

            // Expanded Activities List
            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    activities.forEach { act ->
                        val isDone = completedActivityIds.contains(act.id)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDone) NeonEmerald.copy(alpha = 0.12f) else CyberSurfaceVariant)
                                .border(1.dp, if (isDone) NeonEmerald else CyberBorder, RoundedCornerShape(12.dp))
                                .clickable { onLaunchActivity(act.id) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = if (isDone) NeonEmerald else NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(act.title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                                    Text("Diff: ${act.difficulty}★ | +${act.xpReward} XP", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                }
                            }

                            Button(
                                onClick = { onLaunchActivity(act.id) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isDone) CyberSurfaceLight else NeonCyan,
                                    contentColor = if (isDone) TextPrimary else Color(0xFF00222B)
                                )
                            ) {
                                Text(if (isDone) "REPLAY" else "PLAY", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
