package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AchievementEntity
import com.example.data.local.ActivityAttemptEntity
import com.example.data.local.ActivityEntity
import com.example.data.local.CadetProfileEntity
import com.example.data.local.CodeQuestDatabase
import com.example.data.local.ConceptMasteryEntity
import com.example.data.local.CurriculumModuleEntity
import com.example.data.repository.CurriculumRepository
import com.example.domain.model.AdaptiveRecommendation
import com.example.security.BiometricAuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

sealed class Screen {
    object Onboarding : Screen()
    object Hub : Screen()
    data class ActivityPlayer(val activityId: String) : Screen()
    object BadgeVault : Screen()
    object Resources : Screen()
    data class DocumentReader(val documentId: String) : Screen()
    object ParentDashboard : Screen()
}

data class MainUiState(
    val isLoading: Boolean = true,
    val profile: CadetProfileEntity? = null,
    val modules: List<CurriculumModuleEntity> = emptyList(),
    val activities: List<ActivityEntity> = emptyList(),
    val attempts: List<ActivityAttemptEntity> = emptyList(),
    val masteries: List<ConceptMasteryEntity> = emptyList(),
    val achievements: List<AchievementEntity> = emptyList(),
    val recommendation: AdaptiveRecommendation? = null,
    val currentScreen: Screen = Screen.Onboarding,
    val isParentPinDialogOpen: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = CodeQuestDatabase.getInstance(application)
    private val repository = CurriculumRepository(db.dao())

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeIfNeeded()

            // Observe Profile
            launch {
                repository.getProfile().collectLatest { profile ->
                    _uiState.update { current ->
                        val nextScreen = if (profile == null || !profile.hasCompletedOnboarding) {
                            Screen.Onboarding
                        } else if (current.currentScreen is Screen.Onboarding) {
                            Screen.Hub
                        } else {
                            current.currentScreen
                        }
                        current.copy(
                            profile = profile,
                            currentScreen = nextScreen,
                            isLoading = false
                        )
                    }
                    if (profile != null) {
                        refreshRecommendation(profile.id)
                    }
                }
            }

            // Observe Modules
            launch {
                repository.getModules().collectLatest { mods ->
                    _uiState.update { it.copy(modules = mods) }
                }
            }

            // Observe Activities
            launch {
                db.dao().getActivitiesForModule("mod_basics").collectLatest {
                    val allActs = db.dao().getAllActivitiesOnce()
                    _uiState.update { it.copy(activities = allActs) }
                }
            }

            // Observe Attempts
            launch {
                _uiState.collectLatest { state ->
                    val profileId = state.profile?.id
                    if (profileId != null) {
                        repository.getAttempts(profileId).collectLatest { atts ->
                            _uiState.update { it.copy(attempts = atts) }
                        }
                    }
                }
            }

            // Observe Mastery
            launch {
                _uiState.collectLatest { state ->
                    val profileId = state.profile?.id
                    if (profileId != null) {
                        repository.getMastery(profileId).collectLatest { masts ->
                            _uiState.update { it.copy(masteries = masts) }
                        }
                    }
                }
            }

            // Observe Achievements
            launch {
                _uiState.collectLatest { state ->
                    val profileId = state.profile?.id
                    if (profileId != null) {
                        repository.getAchievements(profileId).collectLatest { achs ->
                            _uiState.update { it.copy(achievements = achs) }
                        }
                    }
                }
            }
        }
    }

    fun completeOnboarding(callSign: String, avatarId: String, parentPin: String?, isBiometricEnabled: Boolean) {
        viewModelScope.launch {
            val newProfile = CadetProfileEntity(
                id = UUID.randomUUID().toString(),
                callSign = callSign,
                avatarId = avatarId,
                totalXp = 50,
                starsEarned = 3,
                level = 1,
                streakDays = 1,
                hasCompletedOnboarding = true,
                parentPinHash = if (!parentPin.isNullOrBlank()) BiometricAuthManager.hashPin(parentPin) else null,
                isBiometricEnabled = isBiometricEnabled,
                createdAt = System.currentTimeMillis()
            )
            repository.saveProfile(newProfile)
            _uiState.update { it.copy(profile = newProfile, currentScreen = Screen.Hub) }
            refreshRecommendation(newProfile.id)
        }
    }

    fun navigateTo(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun openParentGate() {
        _uiState.update { it.copy(isParentPinDialogOpen = true) }
    }

    fun closeParentGate() {
        _uiState.update { it.copy(isParentPinDialogOpen = false) }
    }

    fun onParentGateAuthenticated() {
        _uiState.update { it.copy(isParentPinDialogOpen = false, currentScreen = Screen.ParentDashboard) }
    }

    suspend fun getActivityById(activityId: String) = repository.getActivityById(activityId)

    fun recordAttempt(
        activityId: String,
        conceptId: String,
        isSuccess: Boolean,
        score: Float,
        hintsUsed: Int,
        errorCount: Int,
        timeSpentMs: Long,
        xpReward: Int
    ) {
        val profileId = _uiState.value.profile?.id ?: return
        viewModelScope.launch {
            repository.recordAttemptCompletion(
                profileId = profileId,
                activityId = activityId,
                conceptId = conceptId,
                isSuccess = isSuccess,
                score = score,
                hintsUsed = hintsUsed,
                errorCount = errorCount,
                timeSpentMs = timeSpentMs,
                xpReward = xpReward
            )
            refreshRecommendation(profileId)
        }
    }

    private suspend fun refreshRecommendation(profileId: String) {
        val rec = repository.getAdaptiveRecommendation(profileId)
        _uiState.update { it.copy(recommendation = rec) }
    }
}
