package com.studyinfo.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector

sealed class TopLevelDestination(val route: String, val label: String, val icon: ImageVector) {
    data object Home : TopLevelDestination("home", "Home", Icons.Filled.Home)
    data object Questions : TopLevelDestination("questions", "Questions", Icons.Filled.MenuBook)
    data object Todo : TopLevelDestination("todo", "To-Do", Icons.Filled.Checklist)
    data object Progress : TopLevelDestination("progress", "Progress", Icons.Filled.BarChart)
    data object More : TopLevelDestination("more", "More", Icons.Filled.MoreHoriz)
}

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination.Home,
    TopLevelDestination.Questions,
    TopLevelDestination.Todo,
    TopLevelDestination.Progress,
    TopLevelDestination.More,
)

// ---- Sub-routes (used as string routes inside NavHost) ----
object Routes {
    // Auth
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT = "forgot"

    // Top-level destinations
    const val HOME = "home"
    const val QUESTIONS = "questions"
    const val TODO = "todo"
    const val PROGRESS = "progress"
    const val MORE = "more"

    // Questions sub
    const val ERRORS_LIST = "errors"
    const val ERROR_ADD = "errors/add"
    const val ERROR_DETAIL = "errors/detail/{id}"
    const val ERROR_EDIT = "errors/edit/{id}"
    const val ERROR_REVIEW = "errors/review/{id}"
    fun errorDetail(id: String) = "errors/detail/$id"
    fun errorEdit(id: String) = "errors/edit/$id"
    fun errorReview(id: String) = "errors/review/$id"

    const val UNSOLVED_LIST = "unsolved"
    const val UNSOLVED_ADD = "unsolved/add?source={source}"
    fun unsolvedAdd(source: String? = null): String =
        if (source == null) "unsolved/add?source=" else "unsolved/add?source=$source"
    const val UNSOLVED_DETAIL = "unsolved/detail/{id}"
    const val UNSOLVED_EDIT = "unsolved/edit/{id}"
    fun unsolvedDetail(id: String) = "unsolved/detail/$id"
    fun unsolvedEdit(id: String) = "unsolved/edit/$id"

    const val RETRY_QUEUE = "retry-queue"

    // To-Do
    const val TASK_ADD = "tasks/add"
    const val TASK_EDIT = "tasks/edit/{id}"
    fun taskEdit(id: String) = "tasks/edit/$id"

    // Progress
    const val SUBJECT_PROGRESS = "progress/subject/{subject}"
    fun subjectProgress(subject: String) = "progress/subject/$subject"

    // More
    const val STATISTICS = "statistics"
    const val REVISION = "revision"
    const val SEARCH = "search"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
    const val BACKUP = "backup"
    const val MANAGE_SOURCES = "manage/sources"
    const val MANAGE_CHAPTERS = "manage/chapters"
    const val MANAGE_TAGS = "manage/tags"
    const val ABOUT = "about"
}
