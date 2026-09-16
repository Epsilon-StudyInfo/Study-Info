package com.studyinfo.app.data.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.util.Date

/**
 * Generic Firestore CRUD wrapper for a per-user subcollection.
 *
 * The path used is: `users/{uid}/{collectionName}` (a top-level subcollection under
 * the user document). This matches the layout declared in `firestore.rules`.
 *
 * The mapper/serializer pair is supplied per entity so adding a new synced entity
 * is a one-file operation in the data layer.
 */
class FirestoreCollectionDataSource<T>(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val uidProvider: () -> String?,
    private val collectionName: String,
    private val mapper: (DocumentSnapshot) -> T?,
    private val serializer: (T) -> Map<String, Any?>,
) {
    private fun collection(): CollectionReference? {
        val uid = uidProvider() ?: return null
        return db.collection("${FirestorePaths.userDoc(uid)}/$collectionName")
    }

    fun rawCollection(): CollectionReference? = collection()

    suspend fun fetchAll(): List<T> {
        val col = collection() ?: return emptyList()
        val snap = col.get().await()
        return snap.documents.mapNotNull { mapper(it) }
    }

    suspend fun fetchSince(timestamp: Date): List<T> {
        val col = collection() ?: return emptyList()
        val snap = col.whereGreaterThanOrEqualTo("updatedAt", timestamp).get().await()
        return snap.documents.mapNotNull { mapper(it) }
    }

    suspend fun put(id: String, item: T): Boolean = try {
        val col = collection() ?: return false
        col.document(id).set(serializer(item), SetOptions.merge()).await()
        true
    } catch (e: Exception) { false }

    suspend fun delete(id: String): Boolean = try {
        val col = collection() ?: return false
        col.document(id).delete().await()
        true
    } catch (e: Exception) { false }
}
