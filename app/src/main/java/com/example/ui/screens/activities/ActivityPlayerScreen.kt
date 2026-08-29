package com.example.ui.screens.activities

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ActivityEntity
import com.example.domain.model.ActivityType
import com.example.ui.components.CelebrationDialog
import com.example.ui.components.CyberCard
import com.example.ui.components.StarRatingRow
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberGold
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonEmerald
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityPlayerScreen(
    activityId: String,
    onBack: () -> Unit,
    onActivityFinished: (activityId: String, conceptId: String, isSuccess: Boolean, score: Float, hints: Int, errors: Int, timeMs: Long, xpReward: Int) -> Unit,
    fetchActivity: suspend (String) -> ActivityEntity?
) {
    var activity by remember { mutableStateOf<ActivityEntity?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var showCelebration by remember { mutableStateOf(false) }
    var celebrationXp by remember { mutableIntStateOf(50) }
    var startTime by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(activityId) {
        isLoading = true
        activity = fetchActivity(activityId)
        isLoading = false
        startTime = System.currentTimeMillis()
    }

    if (isLoading || activity == null) {
        Box(
            modifier = Modifier.fillMaxSize().background(CyberBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = NeonCyan)
        }
        return
    }

    val currentActivity = activity!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = currentActivity.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "DIFFICULTY: ${currentActivity.difficulty} ★ | REWARD: +${currentActivity.xpReward} XP",
                            style = MaterialTheme.typography.bodySmall,
                            color = CyberGold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("activity_back_button")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberBackground)
            )
        },
        containerColor = CyberBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Prompt Card
            CyberCard(borderColor = NeonCyan) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.15f))
                    ) {
                        Text("🚀", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(currentActivity.subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        Text(currentActivity.prompt, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sub-Engine View Switcher
            when (currentActivity.type) {
                ActivityType.INPUT_OUTPUT_SORTER -> {
                    InputOutputSorterView(
                        activity = currentActivity,
                        onComplete = { score, hints, errors ->
                            val timeMs = System.currentTimeMillis() - startTime
                            celebrationXp = currentActivity.xpReward
                            showCelebration = true
                            onActivityFinished(currentActivity.id, currentActivity.conceptId, true, score, hints, errors, timeMs, currentActivity.xpReward)
                        }
                    )
                }
                ActivityType.PASSWORD_SHIELD -> {
                    PasswordShieldView(
                        activity = currentActivity,
                        onComplete = { score, hints, errors ->
                            val timeMs = System.currentTimeMillis() - startTime
                            celebrationXp = currentActivity.xpReward
                            showCelebration = true
                            onActivityFinished(currentActivity.id, currentActivity.conceptId, true, score, hints, errors, timeMs, currentActivity.xpReward)
                        }
                    )
                }
                ActivityType.DIGITAL_FOOTPRINT_CHOPPER -> {
                    DigitalFootprintView(
                        activity = currentActivity,
                        onComplete = { score, hints, errors ->
                            val timeMs = System.currentTimeMillis() - startTime
                            celebrationXp = currentActivity.xpReward
                            showCelebration = true
                            onActivityFinished(currentActivity.id, currentActivity.conceptId, true, score, hints, errors, timeMs, currentActivity.xpReward)
                        }
                    )
                }
                ActivityType.MOTHERBOARD_BUILDER -> {
                    MotherboardBuilderView(
                        activity = currentActivity,
                        onComplete = { score, hints, errors ->
                            val timeMs = System.currentTimeMillis() - startTime
                            celebrationXp = currentActivity.xpReward
                            showCelebration = true
                            onActivityFinished(currentActivity.id, currentActivity.conceptId, true, score, hints, errors, timeMs, currentActivity.xpReward)
                        }
                    )
                }
                ActivityType.BINARY_SWITCH -> {
                    BinarySwitchView(
                        activity = currentActivity,
                        onComplete = { score, hints, errors ->
                            val timeMs = System.currentTimeMillis() - startTime
                            celebrationXp = currentActivity.xpReward
                            showCelebration = true
                            onActivityFinished(currentActivity.id, currentActivity.conceptId, true, score, hints, errors, timeMs, currentActivity.xpReward)
                        }
                    )
                }
                ActivityType.OS_VS_APPS -> {
                    OsVsAppsView(
                        activity = currentActivity,
                        onComplete = { score, hints, errors ->
                            val timeMs = System.currentTimeMillis() - startTime
                            celebrationXp = currentActivity.xpReward
                            showCelebration = true
                            onActivityFinished(currentActivity.id, currentActivity.conceptId, true, score, hints, errors, timeMs, currentActivity.xpReward)
                        }
                    )
                }
                ActivityType.PHISHING_INSPECTOR -> {
                    PhishingInspectorView(
                        activity = currentActivity,
                        onComplete = { score, hints, errors ->
                            val timeMs = System.currentTimeMillis() - startTime
                            celebrationXp = currentActivity.xpReward
                            showCelebration = true
                            onActivityFinished(currentActivity.id, currentActivity.conceptId, true, score, hints, errors, timeMs, currentActivity.xpReward)
                        }
                    )
                }
                ActivityType.CODE_BLOCK_SEQUENCER -> {
                    CodeBlockSequencerView(
                        activity = currentActivity,
                        onComplete = { score, hints, errors ->
                            val timeMs = System.currentTimeMillis() - startTime
                            celebrationXp = currentActivity.xpReward
                            showCelebration = true
                            onActivityFinished(currentActivity.id, currentActivity.conceptId, true, score, hints, errors, timeMs, currentActivity.xpReward)
                        }
                    )
                }
                ActivityType.LOOP_COMMANDER -> {
                    LoopCommanderView(
                        activity = currentActivity,
                        onComplete = { score, hints, errors ->
                            val timeMs = System.currentTimeMillis() - startTime
                            celebrationXp = currentActivity.xpReward
                            showCelebration = true
                            onActivityFinished(currentActivity.id, currentActivity.conceptId, true, score, hints, errors, timeMs, currentActivity.xpReward)
                        }
                    )
                }
                ActivityType.BUG_HUNTER -> {
                    BugHunterView(
                        activity = currentActivity,
                        onComplete = { score, hints, errors ->
                            val timeMs = System.currentTimeMillis() - startTime
                            celebrationXp = currentActivity.xpReward
                            showCelebration = true
                            onActivityFinished(currentActivity.id, currentActivity.conceptId, true, score, hints, errors, timeMs, currentActivity.xpReward)
                        }
                    )
                }
                else -> {
                    InputOutputSorterView(
                        activity = currentActivity,
                        onComplete = { score, hints, errors ->
                            val timeMs = System.currentTimeMillis() - startTime
                            celebrationXp = currentActivity.xpReward
                            showCelebration = true
                            onActivityFinished(currentActivity.id, currentActivity.conceptId, true, score, hints, errors, timeMs, currentActivity.xpReward)
                        }
                    )
                }
            }

            if (showCelebration) {
                CelebrationDialog(
                    title = "EXCELLENT CADET!",
                    xpGained = celebrationXp,
                    conceptLearned = currentActivity.title,
                    onContinue = {
                        showCelebration = false
                        onBack()
                    }
                )
            }
        }
    }
}
