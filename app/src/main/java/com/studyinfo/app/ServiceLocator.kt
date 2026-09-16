package com.studyinfo.app

import android.content.Context
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.studyinfo.app.data.auth.SessionManager
import com.studyinfo.app.data.database.PrepVaultDatabase
import com.studyinfo.app.data.firebase.*
import com.studyinfo.app.data.repository.*
import com.studyinfo.app.data.seed.DefaultSeeder
import kotlinx.coroutines.flow.first

/**
 * Tiny manual dependency-injection container.
 *
 * Why not Hilt? Hilt's ksp/kapt configuration adds a meaningful compile-time cost and
 * version-pinning surface. For an MVP with a single Activity, manual wiring is small
 * enough and keeps the build matrix simpler.
 */
object ServiceLocator {

    @Volatile private var initialised = false
    private lateinit var db: PrepVaultDatabase
    lateinit var sessionManager: SessionManager
        private set

    lateinit var authRepository: AuthRepository
        private set
    lateinit var tagRepository: TagRepository
        private set
    lateinit var customSourceRepository: CustomSourceRepository
        private set
    lateinit var chapterRepository: ChapterRepository
        private set
    lateinit var errorRepository: ErrorRepository
        private set
    lateinit var unsolvedRepository: UnsolvedRepository
        private set
    lateinit var taskRepository: TaskRepository
        private set
    lateinit var progressRepository: ProgressRepository
        private set
    lateinit var reviewRepository: ReviewRepository
        private set
    lateinit var questionImageRepository: QuestionImageRepository
        private set
    lateinit var backupRepository: BackupRepository
        private set

    // Firebase singletons. With a placeholder google-services.json these construct fine and
    // simply fail on network calls (caught downstream); if construction itself ever throws
    // (no config), we degrade to local-only mode instead of crashing on launch.
    // FirebaseInitProvider (merged from the google-services plugin) auto-initialises
    // FirebaseApp from resources before the Application runs, so getInstance() is safe here.
    private val firebaseAuthInstance: FirebaseAuth? =
        runCatching { FirebaseAuth.getInstance() }.getOrNull()
    private val firestoreInstance: FirebaseFirestore? =
        runCatching { FirebaseFirestore.getInstance() }.getOrNull()
    private val storageInstance: FirebaseStorage? =
        runCatching { FirebaseStorage.getInstance() }.getOrNull()

    private fun uidProvider(): () -> String? = { firebaseAuthInstance?.currentUser?.uid }

    fun init(context: Context) {
        if (initialised) return
        synchronized(this) {
            if (initialised) return
            db = Room.databaseBuilder(
                context.applicationContext,
                PrepVaultDatabase::class.java,
                PrepVaultDatabase.NAME,
            )
                .fallbackToDestructiveMigration()
                .build()

            sessionManager = SessionManager(context.applicationContext)

            val authDs = if (firebaseAuthInstance != null && firestoreInstance != null) {
                FirebaseAuthDataSource(firebaseAuthInstance, firestoreInstance)
            } else null

            authRepository = AuthRepository(
                firebaseAuth = authDs,
                localAccounts = db.localAccountDao(),
                userProfileDao = db.userProfileDao(),
                session = sessionManager,
                onSwitchDataOwner = { wipeUserData() },
            )

            val streakRecorder = StreakRecorder(db.streakDao())

            tagRepository = TagRepository(
                db.tagDao(),
                firestoreInstance?.let { FirestoreTagsDataSource(it, uidProvider()) },
            )
            customSourceRepository = CustomSourceRepository(
                db.customSourceDao(),
                firestoreInstance?.let { FirestoreCustomSourcesDataSource(it, uidProvider()) },
            )
            chapterRepository = ChapterRepository(
                db.chapterDao(),
                db.topicDao(),
                firestoreInstance?.let { FirestoreChaptersDataSource(it, uidProvider()) },
                firestoreInstance?.let { FirestoreTopicsDataSource(it, uidProvider()) },
            )
            errorRepository = ErrorRepository(
                db.errorDao(),
                firestoreInstance?.let { FirestoreErrorsDataSource(it, uidProvider()) },
                streakRecorder,
            )
            unsolvedRepository = UnsolvedRepository(
                db.unsolvedDao(),
                firestoreInstance?.let { FirestoreUnsolvedDataSource(it, uidProvider()) },
                firestoreInstance?.let { FirestoreErrorsDataSource(it, uidProvider()) },
                db.errorDao(),
                streakRecorder,
            )
            taskRepository = TaskRepository(
                db.taskDao(),
                firestoreInstance?.let { FirestoreTasksDataSource(it, uidProvider()) },
                db.streakDao(),
                firestoreInstance?.let { FirestoreStreakDataSource(it, uidProvider()) },
                streakRecorder,
            )
            progressRepository = ProgressRepository(
                db.progressDao(),
                firestoreInstance?.let { FirestoreProgressDataSource(it, uidProvider()) },
            )
            reviewRepository = ReviewRepository(
                db.reviewDao(),
                firestoreInstance?.let { FirestoreReviewsDataSource(it, uidProvider()) },
            )
            questionImageRepository = QuestionImageRepository(
                db.questionImageDao(),
                firestoreInstance?.let { FirestoreQuestionImagesDataSource(it, uidProvider()) },
                storageInstance?.let { FirebaseStorageDataSource(it, uidProvider()) },
                db.syncQueueDao(),
            )
            backupRepository = BackupRepository(
                db.userProfileDao(),
                db.tagDao(),
                db.customSourceDao(),
                db.chapterDao(),
                db.topicDao(),
                db.questionImageDao(),
                db.errorDao(),
                db.unsolvedDao(),
                db.taskDao(),
                db.reviewDao(),
                db.progressDao(),
                db.streakDao(),
            )

            initialised = true
        }
    }

    /**
     * Wipes all USER-GENERATED data (keeps accounts, and the chapter reference table which
     * is re-seeded anyway). Called when a different account signs in so data never leaks
     * across accounts on a shared device.
     */
    suspend fun wipeUserData() {
        db.userProfileDao().clear()
        db.tagDao().clear()
        db.customSourceDao().clear()
        db.questionImageDao().clear()
        db.errorDao().clear()
        db.unsolvedDao().clear()
        db.taskDao().clear()
        db.reviewDao().clear()
        db.progressDao().clear()
        db.streakDao().clear()
        db.syncQueueDao().clear()
    }

    /**
     * Seed the chapter database on first launch so the Add Question form has something
     * to show without requiring a network round-trip.
     */
    suspend fun seedIfEmpty(context: Context) {
        if (!::db.isInitialized) return
        val existing = chapterRepository.observeAll().first()
        if (existing.isEmpty()) {
            DefaultSeeder.seedChapters(db.chapterDao())
        }
    }

    fun database(): PrepVaultDatabase = db

    /** Returns true if [init] has been called (so all repositories are populated). */
    fun isInitialised(): Boolean = initialised
}
