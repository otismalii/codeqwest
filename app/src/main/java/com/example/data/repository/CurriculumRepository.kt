package com.example.data.repository

import com.example.data.local.AssessmentEvidenceEntity
import com.example.data.local.ActivityAttemptEntity
import com.example.data.local.CadetProfileEntity
import com.example.data.local.CodeQuestDao
import com.example.data.local.ConceptMasteryEntity
import com.example.domain.engine.AdaptiveLearningEngine
import com.example.domain.model.AdaptiveRecommendation
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class CurriculumRepository(
    private val dao: CodeQuestDao,
    private val adaptiveEngine: AdaptiveLearningEngine = AdaptiveLearningEngine()
) {

    suspend fun initializeIfNeeded() {
        val existingModules = dao.getAllActivitiesOnce()
        if (existingModules.isEmpty()) {
            dao.seedDatabase(
                SeedCurriculumData.modules,
                SeedCurriculumData.concepts,
                SeedCurriculumData.activities
            )
        }
    }

    fun getProfile(): Flow<CadetProfileEntity?> = dao.getActiveProfile()

    suspend fun saveProfile(profile: CadetProfileEntity) {
        dao.saveProfile(profile)
        // Ensure default achievements exist
        val existingAchievements = dao.getAchievements(profile.id)
        dao.insertAchievements(SeedCurriculumData.getDefaultAchievements(profile.id))
    }

    fun getModules() = dao.getModules()

    fun getActivitiesForModule(moduleId: String) = dao.getActivitiesForModule(moduleId)

    suspend fun getActivityById(activityId: String) = dao.getActivityById(activityId)

    fun getAttempts(profileId: String) = dao.getAttemptsForProfile(profileId)

    fun getMastery(profileId: String) = dao.getMasteryForProfile(profileId)

    fun getAchievements(profileId: String) = dao.getAchievements(profileId)

    suspend fun recordAttemptCompletion(
        profileId: String,
        activityId: String,
        conceptId: String,
        isSuccess: Boolean,
        score: Float,
        hintsUsed: Int,
        errorCount: Int,
        timeSpentMs: Long,
        xpReward: Int
    ): Pair<Int, Boolean> { // returns (earnedXp, isFirstSuccess)
        val attemptId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()

        val attempt = ActivityAttemptEntity(
            id = attemptId,
            profileId = profileId,
            activityId = activityId,
            conceptId = conceptId,
            timestamp = now,
            isSuccess = isSuccess,
            score = score,
            hintsUsed = hintsUsed,
            errorCount = errorCount,
            timeSpentMs = timeSpentMs
        )
        dao.recordAttempt(attempt)

        // Calculate mastery delta
        val existingMastery = dao.getConceptMastery(conceptId, profileId)
        val (newMasteryScore, confidence) = adaptiveEngine.calculateNewMastery(
            currentMastery = existingMastery,
            isSuccess = isSuccess,
            score = score,
            hintsUsed = hintsUsed,
            errorCount = errorCount
        )

        val delta = newMasteryScore - (existingMastery?.masteryScore ?: 0.0f)
        val friction = (hintsUsed * 0.1f + errorCount * 0.15f).coerceAtMost(0.5f)

        val evidence = AssessmentEvidenceEntity(
            id = UUID.randomUUID().toString(),
            attemptId = attemptId,
            conceptId = conceptId,
            calculatedMasteryDelta = delta,
            frictionScore = friction,
            timestamp = now
        )
        dao.recordEvidence(evidence)

        val updatedMasteryEntity = ConceptMasteryEntity(
            conceptId = conceptId,
            profileId = profileId,
            masteryScore = newMasteryScore,
            attemptsCount = (existingMastery?.attemptsCount ?: 0) + 1,
            successesCount = (existingMastery?.successesCount ?: 0) + (if (isSuccess) 1 else 0),
            lastUpdated = now,
            confidenceLevel = confidence
        )
        dao.updateMastery(updatedMasteryEntity)

        // Update Profile XP & Stars
        var earnedXp = 0
        var isFirstSuccess = false
        val activeProfile = dao.getActiveProfileOnce()
        if (activeProfile != null) {
            val previousSuccesses = dao.getAllAttemptsForProfileOnce(profileId)
                .filter { it.activityId == activityId && it.isSuccess && it.id != attemptId }
            isFirstSuccess = isSuccess && previousSuccesses.isEmpty()

            earnedXp = if (isSuccess) {
                if (isFirstSuccess) xpReward else (xpReward / 2).coerceAtLeast(15)
            } else 10

            val newTotalXp = activeProfile.totalXp + earnedXp
            val newStars = activeProfile.starsEarned + (if (isFirstSuccess) 3 else if (isSuccess) 1 else 0)
            val newLevel = 1 + (newTotalXp / 150)

            dao.saveProfile(
                activeProfile.copy(
                    totalXp = newTotalXp,
                    starsEarned = newStars,
                    level = newLevel
                )
            )

            // Check achievements
            if (newMasteryScore >= 0.75f) {
                if (conceptId.startsWith("basics")) dao.unlockAchievement("ach_byte_scout", profileId, now)
                if (conceptId.startsWith("hardware")) dao.unlockAchievement("ach_silicon_architect", profileId, now)
                if (conceptId.startsWith("software.phishing")) dao.unlockAchievement("ach_cyber_sentinel", profileId, now)
                if (conceptId.startsWith("software.bug")) dao.unlockAchievement("ach_bug_buster", profileId, now)
                if (conceptId.startsWith("code.repeat")) dao.unlockAchievement("ach_loop_master", profileId, now)
            }
        }

        return Pair(earnedXp, isFirstSuccess)
    }

    suspend fun getAdaptiveRecommendation(profileId: String): AdaptiveRecommendation? {
        val activities = dao.getAllActivitiesOnce()
        val concepts = dao.getAllConceptsOnce()
        val attempts = dao.getAllAttemptsForProfileOnce(profileId)
        val masteries = dao.getAllConceptsOnce().map { concept ->
            dao.getConceptMastery(concept.id, profileId) ?: ConceptMasteryEntity(
                conceptId = concept.id,
                profileId = profileId,
                masteryScore = 0.0f,
                attemptsCount = 0,
                successesCount = 0,
                lastUpdated = 0L,
                confidenceLevel = 0.0f
            )
        }

        return adaptiveEngine.generateRecommendation(
            allActivities = activities,
            allConcepts = concepts,
            masteries = masteries,
            recentAttempts = attempts
        )
    }
}
