package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CodeQuestDao {

    // Profile
    @Query("SELECT * FROM cadet_profiles LIMIT 1")
    fun getActiveProfile(): Flow<CadetProfileEntity?>

    @Query("SELECT * FROM cadet_profiles LIMIT 1")
    suspend fun getActiveProfileOnce(): CadetProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: CadetProfileEntity)

    // Curriculum
    @Query("SELECT * FROM curriculum_modules ORDER BY orderIndex ASC")
    fun getModules(): Flow<List<CurriculumModuleEntity>>

    @Query("SELECT * FROM concepts WHERE moduleId = :moduleId")
    fun getConceptsForModule(moduleId: String): Flow<List<ConceptEntity>>

    @Query("SELECT * FROM concepts")
    suspend fun getAllConceptsOnce(): List<ConceptEntity>

    @Query("SELECT * FROM activities WHERE moduleId = :moduleId ORDER BY difficulty ASC")
    fun getActivitiesForModule(moduleId: String): Flow<List<ActivityEntity>>

    @Query("SELECT * FROM activities WHERE id = :activityId LIMIT 1")
    suspend fun getActivityById(activityId: String): ActivityEntity?

    @Query("SELECT * FROM activities")
    suspend fun getAllActivitiesOnce(): List<ActivityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModules(modules: List<CurriculumModuleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConcepts(concepts: List<ConceptEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: List<ActivityEntity>)

    // Attempts & Evidence
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordAttempt(attempt: ActivityAttemptEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordEvidence(evidence: AssessmentEvidenceEntity)

    @Query("SELECT * FROM activity_attempts WHERE profileId = :profileId ORDER BY timestamp DESC")
    fun getAttemptsForProfile(profileId: String): Flow<List<ActivityAttemptEntity>>

    @Query("SELECT * FROM activity_attempts WHERE profileId = :profileId")
    suspend fun getAllAttemptsForProfileOnce(profileId: String): List<ActivityAttemptEntity>

    // Concept Mastery
    @Query("SELECT * FROM concept_mastery WHERE profileId = :profileId")
    fun getMasteryForProfile(profileId: String): Flow<List<ConceptMasteryEntity>>

    @Query("SELECT * FROM concept_mastery WHERE conceptId = :conceptId AND profileId = :profileId LIMIT 1")
    suspend fun getConceptMastery(conceptId: String, profileId: String): ConceptMasteryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMastery(mastery: ConceptMasteryEntity)

    // Achievements
    @Query("SELECT * FROM cadet_achievements WHERE profileId = :profileId")
    fun getAchievements(profileId: String): Flow<List<AchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Query("UPDATE cadet_achievements SET isUnlocked = 1, unlockedAt = :timestamp WHERE id = :achievementId AND profileId = :profileId")
    suspend fun unlockAchievement(achievementId: String, profileId: String, timestamp: Long)

    @Transaction
    suspend fun seedDatabase(
        modules: List<CurriculumModuleEntity>,
        concepts: List<ConceptEntity>,
        activities: List<ActivityEntity>
    ) {
        insertModules(modules)
        insertConcepts(concepts)
        insertActivities(activities)
    }
}
