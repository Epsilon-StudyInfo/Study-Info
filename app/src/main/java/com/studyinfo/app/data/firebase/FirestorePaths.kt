package com.studyinfo.app.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * Firestore path helpers. Centralised so changing the schema in one place is enough.
 *
 * All user data lives under `users/{uid}/...` so the security rules can rely on the
 * `uid` wildcard being equal to `request.auth.uid`.
 */
object FirestorePaths {
    const val USERS = "users"

    fun userDoc(uid: String) = "$USERS/$uid"

    fun errors(uid: String) = "$USERS/$uid/errors"
    fun unsolved(uid: String) = "$USERS/$uid/unsolved"
    fun tasks(uid: String) = "$USERS/$uid/tasks"
    fun reviews(uid: String) = "$USERS/$uid/reviews"
    fun progress(uid: String) = "$USERS/$uid/progress"
    fun tags(uid: String) = "$USERS/$uid/tags"
    fun customSources(uid: String) = "$USERS/$uid/customSources"
    fun chapters(uid: String) = "$USERS/$uid/chapters"
    fun topics(uid: String) = "$USERS/$uid/topics"
    fun settings(uid: String) = "$USERS/$uid/settings"
    fun streak(uid: String) = "$USERS/$uid/streak"

    /**
     * Best-effort recursive delete of the user document subtree.
     *
     * Firestore does not natively support deleting subcollections in a single call;
     * we batch-delete top-level subcollections one document at a time. For production
     * scale (>1k documents per subcollection), use the Firebase Admin `firestore:delete`
     * CLI from a Cloud Function — but for our scale this is fine.
     */
    suspend fun deleteUserTree(db: FirebaseFirestore, uid: String) {
        val subcollections = listOf(
            "errors", "unsolved", "tasks", "reviews", "progress",
            "tags", "customSources", "chapters", "topics", "settings", "streak",
            "questionImages",
        )
        for (sub in subcollections) {
            try {
                val snapshot = db.collection("$USERS/$uid/$sub").get().await()
                val batch = db.batch()
                for (doc in snapshot.documents) batch.delete(doc.reference)
                if (snapshot.documents.isNotEmpty()) batch.commit().await()
            } catch (_: Exception) {
                // best-effort; the next best thing is to leave no-op
            }
        }
        try { db.collection(USERS).document(uid).delete().await() } catch (_: Exception) {}
    }
}
