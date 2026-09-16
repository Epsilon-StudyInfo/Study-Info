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
}
