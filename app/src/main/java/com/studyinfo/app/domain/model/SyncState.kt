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
    PENDING_DELETE,
}
