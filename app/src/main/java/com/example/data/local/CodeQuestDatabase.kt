package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.domain.model.ActivityType
import com.example.domain.model.ModuleCategory

class Converters {
    @TypeConverter
    fun fromModuleCategory(value: ModuleCategory): String = value.name

    @TypeConverter
    fun toModuleCategory(value: String): ModuleCategory = enumValueOf<ModuleCategory>(value)

    @TypeConverter
    fun fromActivityType(value: ActivityType): String = value.name

    @TypeConverter
    fun toActivityType(value: String): ActivityType = enumValueOf<ActivityType>(value)
}

@Database(
    entities = [
        CadetProfileEntity::class,
        CurriculumModuleEntity::class,
        ConceptEntity::class,
        ActivityEntity::class,
        ActivityAttemptEntity::class,
        AssessmentEvidenceEntity::class,
        ConceptMasteryEntity::class,
        AchievementEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CodeQuestDatabase : RoomDatabase() {
    abstract fun dao(): CodeQuestDao

    companion object {
        @Volatile
        private var INSTANCE: CodeQuestDatabase? = null

        fun getInstance(context: Context): CodeQuestDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CodeQuestDatabase::class.java,
                    "codequest_offline_master.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
