package com.studyinfo.app.domain.model

/**
 * Common sync state for any locally-stored entity that mirrors a Firestore document.
 */
enum class SyncState {
    /** Local and remote are in sync. */
    SYNCED,

    /** Created locally, not yet pushed. */
    PENDING_CREATE,

    /** Modified locally, not yet pushed. */
    PENDING_UPDATE,

    /** Deleted locally, still needs to be deleted on remote. */
    PENDING_DELETE;

    companion object {
        /** Never throws: an unknown persisted value (e.g. from a future schema or an import)
         *  degrades to SYNCED instead of crashing Room on read. */
        fun fromName(name: String?): SyncState =
            entries.firstOrNull { it.name.equals(name, ignoreCase = true) } ?: SYNCED
    }
}
