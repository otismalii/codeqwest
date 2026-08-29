package com.example.domain.model

enum class ResourceCategory(val title: String, val tag: String, val icon: String) {
    HISTORY_PIONEERS("Computing Pioneers", "History", "HistoryEdu"),
    GEOGRAPHY_INFRA("Global Tech Geography", "Geography", "Public"),
    EVERYDAY_ANALOGIES("No-Jargon Concepts", "Intuition", "Lightbulb"),
    CYBER_SLEUTH("Cyber Detective & Memes", "Culture & Security", "Security"),
    ALGORITHMS_LORE("Algorithms & Tech Lore", "Logic & Culture", "Code")
}

data class IntuitionQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val memeSnark: String = ""
)

data class ResourceDocument(
    val id: String,
    val title: String,
    val subtitle: String,
    val authorOrSource: String,
    val category: ResourceCategory,
    val readTimeMinutes: Int,
    val publicationYear: String,
    val geoTag: String = "",
    val funMemeQuote: String,
    val summary: String,
    val sections: List<DocumentSection>,
    val intuitionChallenge: IntuitionQuestion
)

data class DocumentSection(
    val heading: String,
    val body: String,
    val calloutBox: String? = null,
    val realWorldFact: String? = null,
    val memeInsight: String? = null
)
