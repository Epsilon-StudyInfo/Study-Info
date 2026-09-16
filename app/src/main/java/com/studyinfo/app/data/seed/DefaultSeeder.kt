package com.studyinfo.app.data.seed

import com.studyinfo.app.data.database.dao.ChapterDao
import com.studyinfo.app.data.database.entity.ChapterEntity
import com.studyinfo.app.domain.model.ExamType
import com.studyinfo.app.domain.model.Subject
import com.studyinfo.app.domain.model.SyncState
import com.studyinfo.app.utils.newId
import com.studyinfo.app.utils.nowEpoch
import java.util.Date

/**
 * Default JEE chapter database seed.
 *
 * These rows are inserted on first launch (only if the chapters table is empty) so the
 * "Add Question" form has a non-empty chapter picker out of the box. The user can rename
 * or delete any of them, and add their own custom chapters.
 *
 * Chapter ids are deterministic so the user can re-seed without duplicates.
 */
object DefaultSeeder {

    suspend fun seedChapters(dao: ChapterDao) {
        val now = Date(nowEpoch())
        val rows = mutableListOf<ChapterEntity>()

        var order = 0
        fun add(subject: Subject, name: String, number: Int, examType: ExamType? = null) {
            rows += ChapterEntity(
                id = "seed-${subject.name.lowercase()}-$number",
                subject = subject.name,
                name = name,
                examType = examType?.name,
                chapterNumber = number,
                displayOrder = order++,
                isCustom = false,
                createdAt = now,
                updatedAt = now,
                syncState = SyncState.SYNCED,
            )
        }

        // Physics (JEE Main + Advanced)
        listOf(
            "Units, Dimensions and Measurement",
            "Kinematics",
            "Laws of Motion",
            "Work, Energy and Power",
            "Rotational Motion",
            "Gravitation",
            "Properties of Solids and Liquids",
            "Thermodynamics",
            "Kinetic Theory of Gases",
            "Oscillations and Waves",
            "Electrostatics",
            "Current Electricity",
            "Magnetic Effects of Current",
            "Electromagnetic Induction",
            "Alternating Current",
            "Electromagnetic Waves",
            "Optics",
            "Dual Nature of Matter and Radiation",
            "Atoms and Nuclei",
            "Electronic Devices",
            "Experimental Physics",
        ).forEachIndexed { i, name -> add(Subject.PHYSICS, name, i + 1) }

        // Chemistry
        listOf(
            "Some Basic Concepts of Chemistry",
            "Structure of Atom",
            "Classification of Elements and Periodicity",
            "Chemical Bonding and Molecular Structure",
            "States of Matter",
            "Thermodynamics",
            "Equilibrium",
            "Redox Reactions",
            "Hydrogen",
            "The s-Block Elements",
            "The p-Block Elements",
            "The d- and f-Block Elements",
            "Coordination Compounds",
            "Haloalkanes and Haloarenes",
            "Alcohols, Phenols and Ethers",
            "Aldehydes, Ketones and Carboxylic Acids",
            "Amines",
            "Biomolecules",
            "Polymers",
            "Chemistry in Everyday Life",
            "Organic Chemistry — Basic Principles",
        ).forEachIndexed { i, name -> add(Subject.CHEMISTRY, name, i + 1) }

        // Mathematics
        listOf(
            "Sets, Relations and Functions",
            "Complex Numbers and Quadratic Equations",
            "Matrices and Determinants",
            "Permutations and Combinations",
            "Binomial Theorem",
            "Sequences and Series",
            "Limit, Continuity and Differentiability",
            "Integral Calculus",
            "Differential Equations",
            "Coordinate Geometry",
            "Three-Dimensional Geometry",
            "Vector Algebra",
            "Statistics and Probability",
            "Trigonometry",
            "Mathematical Reasoning",
        ).forEachIndexed { i, name -> add(Subject.MATHEMATICS, name, i + 1) }

        dao.upsertAll(rows)
    }

    /**
     * Generates a fresh UUID — used by callers that need their own new id instead of a seed id.
     */
    fun newCustomChapterId(): String = newId()
}
