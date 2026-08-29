package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.example.security.BiometricAuthManager
import com.example.ui.MainViewModel
import com.example.ui.Screen
import com.example.ui.components.ParentPinDialog
import com.example.ui.screens.activities.ActivityPlayerScreen
import com.example.ui.screens.badges.BadgeVaultScreen
import com.example.ui.screens.hub.QuestHubScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.parent.ParentDashboardScreen
import com.example.ui.screens.resources.DocumentReaderScreen
import com.example.ui.screens.resources.ResourceLibraryScreen
import com.example.ui.theme.CodeQuestTheme
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.NeonCyan

class MainActivity : FragmentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CodeQuestTheme {
                val state by viewModel.uiState.collectAsState()

                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(CyberBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NeonCyan)
                    }
                } else {
                    AnimatedContent(
                        targetState = state.currentScreen,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "screen_transitions"
                    ) { screen ->
                        when (screen) {
                            is Screen.Onboarding -> {
                                OnboardingScreen(
                                    onCompleteOnboarding = { callSign, avatarId, parentPin, isBiometric ->
                                        viewModel.completeOnboarding(callSign, avatarId, parentPin, isBiometric)
                                    }
                                )
                            }
                            is Screen.Hub -> {
                                val profile = state.profile
                                if (profile != null) {
                                    QuestHubScreen(
                                        profile = profile,
                                        modules = state.modules,
                                        activities = state.activities,
                                        attempts = state.attempts,
                                        masteries = state.masteries,
                                        recommendation = state.recommendation,
                                        onLaunchActivity = { activityId ->
                                            viewModel.navigateTo(Screen.ActivityPlayer(activityId))
                                        },
                                        onOpenBadges = { viewModel.navigateTo(Screen.BadgeVault) },
                                        onOpenResources = { viewModel.navigateTo(Screen.Resources) },
                                        onOpenParentDashboard = { viewModel.navigateTo(Screen.ParentDashboard) }
                                    )
                                }
                            }
                            is Screen.ActivityPlayer -> {
                                ActivityPlayerScreen(
                                    activityId = screen.activityId,
                                    onBack = { viewModel.navigateTo(Screen.Hub) },
                                    fetchActivity = { id -> viewModel.getActivityById(id) },
                                    onActivityFinished = { actId, concId, isSuccess, score, hints, errors, timeMs, xpReward ->
                                        viewModel.recordAttempt(
                                            activityId = actId,
                                            conceptId = concId,
                                            isSuccess = isSuccess,
                                            score = score,
                                            hintsUsed = hints,
                                            errorCount = errors,
                                            timeSpentMs = timeMs,
                                            xpReward = xpReward
                                        )
                                    }
                                )
                            }
                            is Screen.BadgeVault -> {
                                BadgeVaultScreen(
                                    achievements = state.achievements,
                                    onBack = { viewModel.navigateTo(Screen.Hub) }
                                )
                            }
                            is Screen.Resources -> {
                                ResourceLibraryScreen(
                                    onSelectDocument = { docId ->
                                        viewModel.navigateTo(Screen.DocumentReader(docId))
                                    },
                                    onNavigateBack = { viewModel.navigateTo(Screen.Hub) }
                                )
                            }
                            is Screen.DocumentReader -> {
                                DocumentReaderScreen(
                                    documentId = screen.documentId,
                                    onBack = { viewModel.navigateTo(Screen.Resources) }
                                )
                            }
                            is Screen.ParentDashboard -> {
                                val profile = state.profile
                                if (profile != null) {
                                    ParentDashboardScreen(
                                        profile = profile,
                                        attempts = state.attempts,
                                        masteries = state.masteries,
                                        onBack = { viewModel.navigateTo(Screen.Hub) }
                                    )
                                }
                            }
                        }
                    }

                    // Parent PIN / Biometric Dialog
                    if (state.isParentPinDialogOpen) {
                        ParentPinDialog(
                            storedHash = state.profile?.parentPinHash,
                            onVerified = { viewModel.onParentGateAuthenticated() },
                            onDismiss = { viewModel.closeParentGate() },
                            onBiometricRequest = if (BiometricAuthManager.isBiometricAvailable(this@MainActivity)) {
                                {
                                    BiometricAuthManager.promptBiometric(
                                        activity = this@MainActivity,
                                        onSuccess = { viewModel.onParentGateAuthenticated() },
                                        onError = { msg ->
                                            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                                        },
                                        onUsePinFallback = {
                                            // Fallback handled in dialog
                                        }
                                    )
                                }
                            } else null
                        )
                    }
                }
            }
        }
    }
}
