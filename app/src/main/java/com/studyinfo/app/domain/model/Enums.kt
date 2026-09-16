package com.studyinfo.app.domain.model

enum class Difficulty(val label: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    VERY_HARD("Very Hard");

    companion object {
        fun fromLabel(label: String?): Difficulty =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: MEDIUM
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
        fun fromLabel(label: String?): MistakeType =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: OTHER
    }
}

enum class ErrorStatus(val label: String) {
    ACTIVE("Active"),
    UNDERSTOOD("Understood"),
    NEEDS_REVISION("Needs Revision"),
    ARCHIVED("Archived");

    companion object {
        fun fromLabel(label: String?): ErrorStatus =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: ACTIVE
    }
}

enum class UnsolvedStatus(val label: String) {
    UNSOLVED("Unsolved"),
    ATTEMPT_AGAIN("Attempt Again"),
    SOLVED("Solved"),
    UNDERSTOOD("Understood"),
    SKIP_PERMANENTLY("Skip Permanently");

    companion object {
        fun fromLabel(label: String?): UnsolvedStatus =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: UNSOLVED
    }
}

enum class TaskStatus(val label: String) {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    SKIPPED("Skipped");

    companion object {
        fun fromLabel(label: String?): TaskStatus =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: PENDING
    }
}

enum class TaskPriority(val label: String, val weight: Int) {
    LOW("Low", 1),
    MEDIUM("Medium", 2),
    HIGH("High", 3),
    URGENT("Urgent", 4);

    companion object {
        fun fromLabel(label: String?): TaskPriority =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: MEDIUM
    }
}

enum class ChapterState(val label: String) {
    NOT_STARTED("Not Started"),
    LEARNING("Learning"),
    PRACTICING("Practicing"),
    COMPLETED("Completed"),
    NEEDS_REVISION("Needs Revision");

    companion object {
        fun fromLabel(label: String?): ChapterState =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: NOT_STARTED
    }
}

enum class ReviewOutcome(val label: String) {
    UNDERSTOOD("Understood"),
    STILL_CONFUSED("Still Confused"),
    NEEDS_REVISION("Needs Revision");

    companion object {
        fun fromLabel(label: String?): ReviewOutcome =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: NEEDS_REVISION
    }
}

enum class ExamType(val label: String) {
    JEE_MAIN("JEE Main"),
    JEE_ADVANCED("JEE Advanced");

    companion object {
        fun fromLabel(label: String?): ExamType =
            entries.firstOrNull { it.label.equals(label, ignoreCase = true) } ?: JEE_MAIN
    }
}
