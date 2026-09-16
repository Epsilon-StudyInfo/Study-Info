package com.studyinfo.app.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EnumsTest {

    @Test fun `Subject fromName returns matching enum`() {
        assertEquals(Subject.PHYSICS, Subject.fromName("PHYSICS"))
        assertEquals(Subject.CHEMISTRY, Subject.fromName("chemistry"))
        assertEquals(Subject.MATHEMATICS, Subject.fromName("Mathematics"))
    }

    @Test fun `Subject fromName returns OTHER for unknown`() {
        assertEquals(Subject.OTHER, Subject.fromName("biology"))
        assertEquals(Subject.OTHER, Subject.fromName(null))
    }

    @Test fun `QuestionSource fromLabel matches display name`() {
        assertEquals(QuestionSource.DPP, QuestionSource.fromLabel("DPP"))
        assertEquals(QuestionSource.MOCK_TEST, QuestionSource.fromLabel("Mock Test"))
        assertEquals(QuestionSource.CUSTOM, QuestionSource.fromLabel("Bogus"))
        assertEquals(QuestionSource.CUSTOM, QuestionSource.fromLabel(null))
    }

    @Test fun `Difficulty round-trips via label`() {
        Difficulty.entries.forEach { d -> assertEquals(d, Difficulty.fromLabel(d.label)) }
    }

    @Test fun `MistakeType round-trips via label`() {
        MistakeType.entries.forEach { m -> assertEquals(m, MistakeType.fromLabel(m.label)) }
    }

    @Test fun `TaskPriority weights are ascending`() {
        val weights = TaskPriority.entries.map { it.weight }
        for (i in 1 until weights.size) {
            assertTrue("Each higher priority must have larger weight", weights[i] > weights[i - 1])
        }
    }

    @Test fun `UnsolvedStatus fromLabel handles null`() {
        assertEquals(UnsolvedStatus.UNSOLVED, UnsolvedStatus.fromLabel(null))
        assertEquals(UnsolvedStatus.SOLVED, UnsolvedStatus.fromLabel("Solved"))
    }

    @Test fun `ExamType fromLabel defaults to JEE Main`() {
        assertEquals(ExamType.JEE_MAIN, ExamType.fromLabel(null))
        assertEquals(ExamType.JEE_ADVANCED, ExamType.fromLabel("JEE Advanced"))
    }

    // ---- Persistence round-trips (REGRESSION) ----
    // Room converters and Firestore mappers persist `.name` (e.g. "TIME_MANAGEMENT"),
    // NOT the display label. These tests pin the name -> fromName round-trip that the
    // old label-based parsing silently corrupted.

    @Test fun `all enums round-trip via name`() {
        Difficulty.entries.forEach { assertEquals(it, Difficulty.fromName(it.name)) }
        MistakeType.entries.forEach { assertEquals(it, MistakeType.fromName(it.name)) }
        ErrorStatus.entries.forEach { assertEquals(it, ErrorStatus.fromName(it.name)) }
        UnsolvedStatus.entries.forEach { assertEquals(it, UnsolvedStatus.fromName(it.name)) }
        TaskStatus.entries.forEach { assertEquals(it, TaskStatus.fromName(it.name)) }
        TaskPriority.entries.forEach { assertEquals(it, TaskPriority.fromName(it.name)) }
        ChapterState.entries.forEach { assertEquals(it, ChapterState.fromName(it.name)) }
        ReviewOutcome.entries.forEach { assertEquals(it, ReviewOutcome.fromName(it.name)) }
        ExamType.entries.forEach { assertEquals(it, ExamType.fromName(it.name)) }
    }

    @Test fun `fromName never throws on garbage input`() {
        Difficulty.fromName("nonsense")
        MistakeType.fromName("nonsense")
        ErrorStatus.fromName("")
        UnsolvedStatus.fromName(null)
        TaskStatus.fromName("??")
        ChapterState.fromName("completed")   // lowercase still matches
        ExamType.fromName("jee_advanced")
        SyncState.fromName("PENDING_DELETE")
        SyncState.fromName("garbage")        // degrades to SYNCED, never throws
    }

    @Test fun `underscore names do not degrade to OTHER anymore`() {
        // The old bug: name "TIME_MANAGEMENT" parsed via fromLabel fell back to OTHER.
        assertEquals(MistakeType.TIME_MANAGEMENT, MistakeType.fromName("TIME_MANAGEMENT"))
        assertEquals(MistakeType.TIME_MANAGEMENT, MistakeType.fromName("Time-management mistake"))
        assertEquals(ErrorStatus.NEEDS_REVISION, ErrorStatus.fromName("NEEDS_REVISION"))
        assertEquals(UnsolvedStatus.SKIP_PERMANENTLY, UnsolvedStatus.fromName("SKIP_PERMANENTLY"))
        assertEquals(TaskStatus.IN_PROGRESS, TaskStatus.fromName("IN_PROGRESS"))
        assertEquals(Difficulty.VERY_HARD, Difficulty.fromName("VERY_HARD"))
        assertEquals(ChapterState.NEEDS_REVISION, ChapterState.fromName("NEEDS_REVISION"))
        assertEquals(ExamType.JEE_ADVANCED, ExamType.fromName("JEE_ADVANCED"))
    }
}
