package com.example.domain.engine

import com.example.data.local.ActivityAttemptEntity
import com.example.data.local.ActivityEntity
import com.example.data.local.ConceptEntity
import com.example.data.local.ConceptMasteryEntity
import com.example.domain.model.AdaptiveRecommendation
import com.example.domain.model.RecommendationRationale

class AdaptiveLearningEngine {

    /**
     * Updates concept mastery following Bayesian-inspired exponential smoothing with friction penalty.
     */
    fun calculateNewMastery(
        currentMastery: ConceptMasteryEntity?,
        isSuccess: Boolean,
        score: Float,
        hintsUsed: Int,
        errorCount: Int
    ): Pair<Float, Float> {
        val previousScore = currentMastery?.masteryScore ?: 0.0f
        val attempts = (currentMastery?.attemptsCount ?: 0) + 1
        val successes = (currentMastery?.successesCount ?: 0) + (if (isSuccess) 1 else 0)

        // Friction penalty: each hint or error reduces the attempt yield slightly
        val friction = (hintsUsed * 0.08f + errorCount * 0.12f).coerceAtMost(0.40f)
        val adjustedScore = (score - friction).coerceIn(0.0f, 1.0f)

        // Learning rate decreases as experience increases (simulates confidence stabilization)
        val alpha = (0.45f / (1.0f + attempts * 0.15f)).coerceIn(0.15f, 0.45f)
        val newMastery = (previousScore * (1.0f - alpha) + adjustedScore * alpha).coerceIn(0.0f, 1.0f)

        val confidence = (attempts.toFloat() / 5.0f).coerceAtMost(1.0f)
        return Pair(newMastery, confidence)
    }

    /**
     * Evaluates current mastery profile and recent attempts to deterministically recommend the next mission.
     */
    fun generateRecommendation(
        allActivities: List<ActivityEntity>,
        allConcepts: List<ConceptEntity>,
        masteries: List<ConceptMasteryEntity>,
        recentAttempts: List<ActivityAttemptEntity>
    ): AdaptiveRecommendation? {
        if (allActivities.isEmpty() || allConcepts.isEmpty()) return null

        val masteryMap = masteries.associateBy { it.conceptId }
        val completedActivityIds = recentAttempts.filter { it.isSuccess }.map { it.activityId }.toSet()

        // Rule 1: Remedy weakest concept if mastery is low (< 0.60) and was attempted
        val weakMastery = masteries
            .filter { it.masteryScore < 0.60f && it.attemptsCount > 0 }
            .minByOrNull { it.masteryScore }

        if (weakMastery != null) {
            val weakConcept = allConcepts.firstOrNull { it.id == weakMastery.conceptId }
            val activityForWeak = allActivities.firstOrNull { it.conceptId == weakMastery.conceptId }
            if (weakConcept != null && activityForWeak != null) {
                return AdaptiveRecommendation(
                    targetActivity = activityForWeak.toDomain(),
                    targetConcept = weakConcept.toDomain(),
                    rationale = RecommendationRationale.STRENGTHEN_WEAK_CONCEPT,
                    confidenceScore = weakMastery.masteryScore
                )
            }
        }

        // Rule 2: Next uncompleted activity in the curriculum sequence
        for (activity in allActivities) {
            if (!completedActivityIds.contains(activity.id)) {
                val concept = allConcepts.firstOrNull { it.id == activity.conceptId } ?: allConcepts.first()
                return AdaptiveRecommendation(
                    targetActivity = activity.toDomain(),
                    targetConcept = concept.toDomain(),
                    rationale = RecommendationRationale.CURRICULUM_PROGRESSION,
                    confidenceScore = masteryMap[concept.id]?.masteryScore ?: 0.0f
                )
            }
        }

        // Rule 3: All done -> Challenge the highest difficulty activity
        val challengeActivity = allActivities.maxByOrNull { it.difficulty } ?: allActivities.first()
        val challengeConcept = allConcepts.firstOrNull { it.id == challengeActivity.conceptId } ?: allConcepts.first()
        return AdaptiveRecommendation(
            targetActivity = challengeActivity.toDomain(),
            targetConcept = challengeConcept.toDomain(),
            rationale = RecommendationRationale.CHALLENGE_MASTERY,
            confidenceScore = 1.0f
        )
    }

    private fun ActivityEntity.toDomain() = com.example.domain.model.ActivityDefinition(
        id = id,
        moduleId = moduleId,
        conceptId = conceptId,
        title = title,
        subtitle = subtitle,
        type = type,
        difficulty = difficulty,
        xpReward = xpReward,
        prompt = prompt,
        contentJson = contentJson
    )

    private fun ConceptEntity.toDomain() = com.example.domain.model.Concept(
        id = id,
        moduleId = moduleId,
        title = title,
        description = description,
        iconName = iconName,
        targetMastery = targetMastery
    )
}
