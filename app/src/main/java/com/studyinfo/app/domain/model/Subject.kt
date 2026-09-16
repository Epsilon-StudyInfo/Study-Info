package com.studyinfo.app.domain.model

/**
 * Core subjects for JEE preparation. Used across errors, unsolved questions, tasks, progress.
 *
 * Values are stored as their ordinal/name in Room and Firestore so adding new subjects is safe.
 */
enum class Subject(val displayName: String) {
    PHYSICS("Physics"),
    CHEMISTRY("Chemistry"),
    MATHEMATICS("Mathematics"),
    OTHER("Other");

    companion object {
        fun fromName(name: String?): Subject =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: OTHER
    }
}
