package com.example.domain

import com.example.domain.model.ActivityType
import com.example.domain.model.ModuleCategory
import com.example.domain.model.RecommendationRationale

/**
 * Formal Domain Model for CodeQuest LDX Nexus.
 * Represents core architecture entities: Concept, Activity, Attempt, Evidence, LearnerModel, and Adaptation.
 */

// =========================================================================
// 1. CONCEPT DOMAIN ENTITY
// =========================================================================
data class Concept(
    val id: String,
    val moduleId: String,
    val title: String,
    val description: String,
    val category: ModuleCategory,
    val iconName: String,
    val targetMastery: Float = 0.75f,
    val prerequisites: List<String> = emptyList(),
    val realWorldAnalogy: String = "",
    val historyFact: String = "",
    val memeReference: String = ""
)

// =========================================================================
// 2. ACTIVITY DOMAIN ENTITY
// =========================================================================
data class Activity(
    val id: String,
    val moduleId: String,
    val conceptId: String,
    val title: String,
    val subtitle: String,
    val type: ActivityType,
    val difficulty: Int, // 1 (Novice) to 5 (Grandmaster)
    val xpReward: Int = 50,
    val prompt: String,
    val contentJson: String = "",
    val hints: List<String> = emptyList(),
    val criticalThinkingHook: String = "",
    val realWorldContext: String = ""
)

// =========================================================================
// 3. ATTEMPT DOMAIN ENTITY
// =========================================================================
data class Attempt(
    val id: String,
    val profileId: String,
    val activityId: String,
    val conceptId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSuccess: Boolean,
    val rawScore: Float, // 0.0 to 1.0
    val effectiveScore: Float, // Score adjusted for friction penalty
    val hintsUsed: Int = 0,
    val errorCount: Int = 0,
    val timeSpentMs: Long = 0L,
    val inputAudit: String = ""
)

// =========================================================================
// 4. EVIDENCE DOMAIN ENTITY
// =========================================================================
data class Evidence(
    val id: String,
    val attemptId: String,
    val conceptId: String,
    val calculatedMasteryDelta: Float,
    val frictionScore: Float,
    val priorMastery: Float,
    val updatedMastery: Float,
    val confidenceWeight: Float = 1.0f,
    val observationNotes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

// =========================================================================
// 5. LEARNER MODEL DOMAIN ENTITY
// =========================================================================
data class LearnerModel(
    val profileId: String,
    val callSign: String,
    val avatarId: String,
    val currentLevel: Int,
    val totalXp: Int,
    val streakDays: Int = 1,
    val conceptMasteries: Map<String, Float> = emptyMap(),
    val completedActivityIds: Set<String> = emptySet(),
    val totalAttemptsCount: Int = 0,
    val overallAccuracyRate: Float = 0.0f,
    val dominantStrengths: List<String> = emptyList(),
    val growthOpportunities: List<String> = emptyList(),
    val learningVelocityScore: Float = 1.0f,
    val lastActiveTimestamp: Long = System.currentTimeMillis()
)

// =========================================================================
// 6. ADAPTATION DOMAIN ENTITY
// =========================================================================
data class Adaptation(
    val targetActivity: Activity,
    val targetConcept: Concept,
    val rationale: RecommendationRationale,
    val confidenceScore: Float,
    val pedagogicalGoal: String,
    val recommendedScaffolding: ScaffoldingLevel = ScaffoldingLevel.STANDARD,
    val difficultyAdjustment: Int = 0 // -1 (easier), 0 (normal), +1 (harder)
)

enum class ScaffoldingLevel(val label: String, val hintAvailability: String) {
    HIGH_SUPPORT("Guided Cadet Support", "Proactive step-by-step hints and visual anchors"),
    STANDARD("Standard Explorer", "On-demand hints with mild friction penalty"),
    CHALLENGE("Mastery Challenge", "Zero hints, double XP reward for precision")
}
