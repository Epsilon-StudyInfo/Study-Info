package com.studyinfo.app.domain.model

/**
 * Enum parsing contract used across Room converters, Firestore mappers and backup (de)serialisation.
 *
 * IMPORTANT: values are persisted as [Enum.name] (e.g. "TIME_MANAGEMENT"), NOT as their display
 * label. [fromName] must be used everywhere a persisted string is parsed back. [fromLabel] is
 * only for UI-driven input. Both fall back to the enum default instead of throwing, so a bad
 * legacy value can never crash the app.
 */
enum class Difficulty(val label: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    VERY_HARD("Very Hard");

    companion object {
        val DEFAULT = MEDIUM
        fun fromName(name: String?): Difficulty =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: fromLabel(name)

        fun fromLabel(label: String?): Difficulty =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: DEFAULT
    }
}

enum class MistakeType(val label: String) {
    CONCEPTUAL("Conceptual mistake"),
    CALCULATION("Calculation mistake"),
    SILLY("Silly mistake"),
    FORMULA("Formula mistake"),
    MISREAD("Misread question"),
    TIME_MANAGEMENT("Time-management mistake"),
    GUESSING("Guessing mistake"),
    OTHER("Other");

    companion object {
        val DEFAULT = OTHER
        fun fromName(name: String?): MistakeType =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: fromLabel(name)

        fun fromLabel(label: String?): MistakeType =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: DEFAULT
    }
}

enum class ErrorStatus(val label: String) {
    ACTIVE("Active"),
    UNDERSTOOD("Understood"),
    NEEDS_REVISION("Needs Revision"),
    ARCHIVED("Archived");

    companion object {
        val DEFAULT = ACTIVE
        fun fromName(name: String?): ErrorStatus =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: fromLabel(name)

        fun fromLabel(label: String?): ErrorStatus =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: DEFAULT
    }
}

enum class UnsolvedStatus(val label: String) {
    UNSOLVED("Unsolved"),
    ATTEMPT_AGAIN("Attempt Again"),
    SOLVED("Solved"),
    UNDERSTOOD("Understood"),
    SKIP_PERMANENTLY("Skip Permanently");

    companion object {
        val DEFAULT = UNSOLVED
        fun fromName(name: String?): UnsolvedStatus =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: fromLabel(name)

        fun fromLabel(label: String?): UnsolvedStatus =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: DEFAULT
    }
}

enum class TaskStatus(val label: String) {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    SKIPPED("Skipped");

    companion object {
        val DEFAULT = PENDING
        fun fromName(name: String?): TaskStatus =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: fromLabel(name)

        fun fromLabel(label: String?): TaskStatus =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: DEFAULT
    }
}

enum class TaskPriority(val label: String, val weight: Int) {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    URGENT("Urgent", 4);

    companion object {
        val DEFAULT = MEDIUM
        fun fromName(name: String?): TaskPriority =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: fromLabel(name)

        fun fromLabel(label: String?): TaskPriority =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: DEFAULT
    }
}

enum class ChapterState(val label: String) {
    NOT_STARTED("Not Started"),
    LEARNING("Learning"),
    PRACTICING("Practicing"),
    COMPLETED("Completed"),
    NEEDS_REVISION("Needs Revision");

    companion object {
        val DEFAULT = NOT_STARTED
        fun fromName(name: String?): ChapterState =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: fromLabel(name)

        fun fromLabel(label: String?): ChapterState =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: DEFAULT
    }
}

enum class ReviewOutcome(val label: String) {
    UNDERSTOOD("Understood"),
    STILL_CONFUSED("Still Confused"),
    NEEDS_REVISION("Needs Revision");

    companion object {
        val DEFAULT = NEEDS_REVISION
        fun fromName(name: String?): ReviewOutcome =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: fromLabel(name)

        fun fromLabel(label: String?): ReviewOutcome =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: DEFAULT
    }
}

enum class ExamType(val label: String) {
    JEE_MAIN("JEE Main"),
    JEE_ADVANCED("JEE Advanced");

    companion object {
        val DEFAULT = JEE_MAIN
        fun fromName(name: String?): ExamType =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: fromLabel(name)

        fun fromLabel(label: String?): ExamType =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: DEFAULT
    }
}
