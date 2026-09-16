package com.studyinfo.app

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    private fun uidProvider(): () -> String? = { auth.currentUser?.uid }

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

            val authDs = FirebaseAuthDataSource(auth, firestore)
            authRepository = AuthRepository(authDs, db.userProfileDao())

            tagRepository = TagRepository(
                db.tagDao(),
                FirestoreTagsDataSource(firestore, uidProvider()),
            )
            customSourceRepository = CustomSourceRepository(
                db.customSourceDao(),
                FirestoreCustomSourcesDataSource(firestore, uidProvider()),
            )
            chapterRepository = ChapterRepository(
                db.chapterDao(),
                db.topicDao(),
                FirestoreChaptersDataSource(firestore, uidProvider()),
                FirestoreTopicsDataSource(firestore, uidProvider()),
            )
            errorRepository = ErrorRepository(
                db.errorDao(),
                FirestoreErrorsDataSource(firestore, uidProvider()),
            )
            unsolvedRepository = UnsolvedRepository(
                db.unsolvedDao(),
                FirestoreUnsolvedDataSource(firestore, uidProvider()),
                FirestoreErrorsDataSource(firestore, uidProvider()),
                db.errorDao(),
            )
            taskRepository = TaskRepository(
                db.taskDao(),
                FirestoreTasksDataSource(firestore, uidProvider()),
                db.streakDao(),
                FirestoreStreakDataSource(firestore, uidProvider()),
            )
            progressRepository = ProgressRepository(
                db.progressDao(),
                FirestoreProgressDataSource(firestore, uidProvider()),
            )
            reviewRepository = ReviewRepository(
                db.reviewDao(),
                FirestoreReviewsDataSource(firestore, uidProvider()),
            )
            questionImageRepository = QuestionImageRepository(
                db.questionImageDao(),
                FirestoreQuestionImagesDataSource(firestore, uidProvider()),
                FirebaseStorageDataSource(storage, uidProvider()),
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
