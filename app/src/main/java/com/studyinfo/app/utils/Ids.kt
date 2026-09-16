package com.studyinfo.app.utils

import java.util.UUID

/**
 * Generates a UUID suitable for use as a Firestore document id AND a Room primary key.
 *
 * Using the same id locally and remotely avoids the "I just created it online and now
 * my local copy is a duplicate" problem.
 */
fun newId(): String = UUID.randomUUID().toString()

fun nowEpoch(): Long = System.currentTimeMillis()
