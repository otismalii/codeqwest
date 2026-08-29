package com.example.domain.model

enum class ModuleCategory(val title: String, val badgeName: String) {
    BASICS("Computer Basics", "Byte Scout"),
    HARDWARE("Hardware Lab", "Silicon Engineer"),
    SOFTWARE("Software World", "App Navigator"),
    CODE("Code Academy", "Algorithm Ace")
}

enum class ActivityType {
    INPUT_OUTPUT_SORTER,
    PASSWORD_SHIELD,
    DIGITAL_FOOTPRINT_CHOPPER,
    MOTHERBOARD_BUILDER,
    BINARY_SWITCH,
    PORTS_AND_CABLES,
    OS_VS_APPS,
    BUG_HUNTER,
    PHISHING_INSPECTOR,
    CODE_BLOCK_SEQUENCER,
    LOOP_COMMANDER,
    IF_ELSE_ROVER
}

data class Concept(
    val id: String,
    val moduleId: String,
    val title: String,
    val description: String,
    val iconName: String,
    val targetMastery: Float = 0.75f
)

data class CurriculumModule(
    val id: String,
    val title: String,
    val description: String,
    val category: ModuleCategory,
    val orderIndex: Int,
    val iconName: String,
    val concepts: List<Concept> = emptyList()
)

data class ActivityDefinition(
    val id: String,
    val moduleId: String,
    val conceptId: String,
    val title: String,
    val subtitle: String,
    val type: ActivityType,
    val difficulty: Int, // 1 to 5
    val xpReward: Int = 50,
    val prompt: String,
    val contentJson: String = ""
)

data class ActivityAttempt(
    val id: String,
    val profileId: String,
    val activityId: String,
    val conceptId: String,
    val timestamp: Long,
    val isSuccess: Boolean,
    val score: Float, // 0.0 to 1.0
    val hintsUsed: Int,
    val errorCount: Int,
    val timeSpentMs: Long
)

data class AssessmentEvidence(
    val id: String,
    val attemptId: String,
    val conceptId: String,
    val calculatedMasteryDelta: Float,
    val frictionScore: Float,
    val timestamp: Long
)

data class ConceptMastery(
    val conceptId: String,
    val profileId: String,
    val masteryScore: Float, // 0.0 to 1.0
    val attemptsCount: Int,
    val successesCount: Int,
    val lastUpdated: Long,
    val confidenceLevel: Float // 0.0 to 1.0
)

enum class RecommendationRationale(val tag: String, val explanation: String) {
    CURRICULUM_PROGRESSION("Next Mission", "Continue your cadet journey step-by-step"),
    STRENGTHEN_WEAK_CONCEPT("Power-Up Needed", "Reviewing this concept will boost your cadet shield!"),
    CHALLENGE_MASTERY("Elite Challenge", "You're mastering this! Test your elite cadet skills"),
    CONFIDENCE_BOOST("Quick Refresh", "A fun exercise to keep your knowledge fresh and charged")
}

data class AdaptiveRecommendation(
    val targetActivity: ActivityDefinition,
    val targetConcept: Concept,
    val rationale: RecommendationRationale,
    val confidenceScore: Float
)

data class CadetProfile(
    val id: String,
    val callSign: String,
    val avatarId: String,
    val totalXp: Int = 0,
    val starsEarned: Int = 0,
    val level: Int = 1,
    val streakDays: Int = 1,
    val hasCompletedOnboarding: Boolean = false,
    val parentPinHash: String? = null,
    val isBiometricEnabled: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

data class CadetBadge(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean,
    val unlockedAt: Long? = null
)
