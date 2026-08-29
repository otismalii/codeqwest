package com.example

import com.example.data.local.ActivityEntity
import com.example.data.local.ConceptMasteryEntity
import com.example.data.repository.CurriculumSeedData
import com.example.domain.engine.AdaptiveLearningEngine
import com.example.domain.model.ActivityType
import com.example.domain.model.RecommendationRationale
import com.example.security.BiometricAuthManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CodeQuestEngineTest {

    @Test
    fun pinHashing_isConsistentAndSecure() {
        val pin = "1234"
        val hash1 = BiometricAuthManager.hashPin(pin)
        val hash2 = BiometricAuthManager.hashPin(pin)

        assertEquals(hash1, hash2)
        assertTrue(BiometricAuthManager.verifyPin("1234", hash1))
        assertTrue(!BiometricAuthManager.verifyPin("9999", hash1))
    }

    @Test
    fun adaptiveEngine_computesEffectiveScoreWithFrictionPenalty() {
        // Raw score 1.0, 1 hint (-0.10), 1 error (-0.05) -> 0.85
        val effectiveScore = AdaptiveLearningEngine.calculateEffectiveScore(
            rawScore = 1.0f,
            hintsUsed = 1,
            errorCount = 1
        )
        assertEquals(0.85f, effectiveScore, 0.01f)

        // Perfect score (0 hints, 0 errors) -> 1.0
        val perfectScore = AdaptiveLearningEngine.calculateEffectiveScore(
            rawScore = 1.0f,
            hintsUsed = 0,
            errorCount = 0
        )
        assertEquals(1.0f, perfectScore, 0.001f)
    }

    @Test
    fun adaptiveEngine_updatesMasteryWithExponentialSmoothing() {
        val currentMastery = 0.50f
        val effectiveScore = 1.00f
        val newMastery = AdaptiveLearningEngine.updateMastery(currentMastery, effectiveScore)

        // 0.50 * 0.65 + 1.00 * 0.35 = 0.325 + 0.350 = 0.675
        assertEquals(0.675f, newMastery, 0.01f)
    }

    @Test
    fun adaptiveEngine_recommendsNextActivityDeterministically() {
        val activities = CurriculumSeedData.getSeedActivities()
        val masteries = listOf(
            ConceptMasteryEntity(
                id = "cm_1",
                profileId = "cadet_1",
                conceptId = "conc_io",
                masteryScore = 0.40f, // weak concept (< 0.70)
                attemptsCount = 2,
                lastPracticedTimestamp = System.currentTimeMillis()
            )
        )

        val completedActivities = emptySet<String>()

        val rec = AdaptiveLearningEngine.recommendNextActivity(
            activities = activities,
            completedActivityIds = completedActivities,
            currentMasteries = masteries
        )

        assertNotNull(rec)
        // Should recommend strengthening the weak concept first
        assertEquals(RecommendationRationale.STRENGTHEN_WEAK_CONCEPT, rec.rationale)
        assertEquals("conc_io", rec.targetActivity.conceptId)
    }

    @Test
    fun curriculumSeedData_hasAllFourModulesAndCompleteActivities() {
        val modules = CurriculumSeedData.getSeedModules()
        val activities = CurriculumSeedData.getSeedActivities()
        val achievements = CurriculumSeedData.getSeedAchievements("test_profile")

        assertEquals(4, modules.size)
        assertTrue(activities.size >= 10)
        assertTrue(achievements.size >= 6)

        // Verify each activity has a valid type and non-empty prompt
        activities.forEach { act ->
            assertTrue(act.title.isNotBlank())
            assertTrue(act.prompt.isNotBlank())
            assertTrue(act.xpReward > 0)
        }
    }

    @Test
    fun formalDomainModels_instantiateCorrectly() {
        val concept = com.example.domain.Concept(
            id = "c_1",
            moduleId = "m_1",
            title = "Binary Logic",
            description = "1s and 0s",
            category = com.example.domain.model.ModuleCategory.HARDWARE,
            iconName = "Memory",
            realWorldAnalogy = "Light switches on the wall"
        )
        assertEquals("Binary Logic", concept.title)

        val learner = com.example.domain.LearnerModel(
            profileId = "cadet_99",
            callSign = "PixelNova",
            avatarId = "nova_explorer",
            currentLevel = 2,
            totalXp = 180,
            streakDays = 3
        )
        assertEquals(2, learner.currentLevel)
        assertEquals("PixelNova", learner.callSign)

        val evidence = com.example.domain.Evidence(
            id = "ev_1",
            attemptId = "att_1",
            conceptId = "c_1",
            calculatedMasteryDelta = 0.15f,
            frictionScore = 0.05f,
            priorMastery = 0.50f,
            updatedMastery = 0.65f
        )
        assertEquals(0.15f, evidence.calculatedMasteryDelta, 0.001f)
    }

    @Test
    fun resourceLibrary_hasCuratedDocumentsAndValidIntuitionChallenges() {
        val docs = com.example.data.repository.ResourceLibraryData.getAllDocuments()
        assertTrue(docs.size >= 5)

        docs.forEach { doc ->
            assertTrue(doc.title.isNotBlank())
            assertTrue(doc.sections.isNotEmpty())
            assertTrue(doc.summary.isNotBlank())
            assertTrue(doc.readTimeMinutes > 0)

            // Validate intuition challenges
            val challenge = doc.intuitionChallenge
            assertTrue(challenge.question.isNotBlank())
            assertTrue(challenge.options.size >= 2)
            assertTrue(challenge.correctIndex in challenge.options.indices)
            assertTrue(challenge.explanation.isNotBlank())
        }
    }
}
