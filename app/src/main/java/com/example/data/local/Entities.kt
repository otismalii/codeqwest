package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.ActivityType
import com.example.domain.model.ModuleCategory

@Entity(tableName = "cadet_profiles")
data class CadetProfileEntity(
    @PrimaryKey val id: String,
    val callSign: String,
    val avatarId: String,
    val totalXp: Int,
    val starsEarned: Int,
    val level: Int,
    val streakDays: Int,
    val hasCompletedOnboarding: Boolean,
    val parentPinHash: String?,
    val isBiometricEnabled: Boolean,
    val createdAt: Long
)

@Entity(tableName = "curriculum_modules")
data class CurriculumModuleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val category: ModuleCategory,
    val orderIndex: Int,
    val iconName: String
)

@Entity(tableName = "concepts")
data class ConceptEntity(
    @PrimaryKey val id: String,
    val moduleId: String,
    val title: String,
    val description: String,
    val iconName: String,
    val targetMastery: Float
)

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey val id: String,
    val moduleId: String,
    val conceptId: String,
    val title: String,
    val subtitle: String,
    val type: ActivityType,
    val difficulty: Int,
    val xpReward: Int,
    val prompt: String,
    val contentJson: String
)

@Entity(tableName = "activity_attempts")
data class ActivityAttemptEntity(
    @PrimaryKey val id: String,
    val profileId: String,
    val activityId: String,
    val conceptId: String,
    val timestamp: Long,
    val isSuccess: Boolean,
    val score: Float,
    val hintsUsed: Int,
    val errorCount: Int,
    val timeSpentMs: Long
)

@Entity(tableName = "assessment_evidence")
data class AssessmentEvidenceEntity(
    @PrimaryKey val id: String,
    val attemptId: String,
    val conceptId: String,
    val calculatedMasteryDelta: Float,
    val frictionScore: Float,
    val timestamp: Long
)

@Entity(tableName = "concept_mastery", primaryKeys = ["conceptId", "profileId"])
data class ConceptMasteryEntity(
    val conceptId: String,
    val profileId: String,
    val masteryScore: Float,
    val attemptsCount: Int,
    val successesCount: Int,
    val lastUpdated: Long,
    val confidenceLevel: Float
)

@Entity(tableName = "cadet_achievements", primaryKeys = ["id", "profileId"])
data class AchievementEntity(
    val id: String,
    val profileId: String,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean,
    val unlockedAt: Long?
)
