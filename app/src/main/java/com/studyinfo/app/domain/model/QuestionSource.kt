package com.studyinfo.app.domain.model

/**
 * Where a question originated from.
 *
 * The app supports DPP / PYQ / Module / Mock Test / Book / Custom sources.
 * Each source type carries its own dynamic metadata fields (see [SourceDetails]).
 */
enum class QuestionSource(val displayName: String) {
    DPP("DPP"),
    PYQ("PYQ"),
    MODULE("Module"),
    MOCK_TEST("Mock Test"),
    BOOK("Book"),
    CUSTOM("Custom");

    companion object {
        fun fromName(name: String?): QuestionSource =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: CUSTOM

        fun fromLabel(label: String?): QuestionSource =
            entries.firstOrNull { it.displayName.equals(label, ignoreCase = true) } ?: CUSTOM
    }
}
