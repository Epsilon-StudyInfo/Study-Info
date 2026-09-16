package com.studyinfo.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.studyinfo.app.navigation.Routes
import com.studyinfo.app.navigation.TOP_LEVEL_DESTINATIONS
import com.studyinfo.app.ui.about.AboutScreen
import com.studyinfo.app.ui.auth.*
import com.studyinfo.app.ui.backup.BackupScreen
import com.studyinfo.app.ui.errors.*
import com.studyinfo.app.ui.home.HomeScreen
import com.studyinfo.app.ui.manage.*
import com.studyinfo.app.ui.more.MoreScreen
import com.studyinfo.app.ui.progress.ProgressScreen
import com.studyinfo.app.ui.progress.SubjectProgressScreen
import com.studyinfo.app.ui.questions.QuestionsHubScreen
import com.studyinfo.app.ui.revision.RevisionScreen
import com.studyinfo.app.ui.search.SearchScreen
import com.studyinfo.app.ui.settings.ProfileScreen
import com.studyinfo.app.ui.settings.SettingsScreen
import com.studyinfo.app.ui.statistics.StatisticsScreen
import com.studyinfo.app.ui.todo.TaskEditScreen
import com.studyinfo.app.ui.todo.TodoListScreen
import com.studyinfo.app.ui.unsolved.*

@Composable
fun PrepVaultRoot() {
    val nav = rememberNavController()
    val authState = remember { mutableStateOf(com.studyinfo.app.ServiceLocator.authRepository.isSignedIn()) }

    LaunchedEffect(Unit) {
        // Re-evaluate auth state when this composable first runs.
        authState.value = com.studyinfo.app.ServiceLocator.authRepository.isSignedIn()
    }

    val startDestination = if (authState.value) Routes.HOME else Routes.SPLASH

    Scaffold(
        bottomBar = {
            val currentBackStackEntry by nav.currentBackStackEntryAsState()
            val currentRoute = currentBackStackEntry?.destination?.route
            val showBottomBar = currentRoute in TOP_LEVEL_DESTINATIONS.map { it.route }
            if (showBottomBar) {
                NavigationBar {
                    TOP_LEVEL_DESTINATIONS.forEach { dest ->
                        NavigationBarItem(
                            selected = currentRoute == dest.route,
                            onClick = {
                                nav.navigate(dest.route) {
                                    popUpTo(Routes.HOME) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(dest.icon, contentDescription = dest.label) },
                            label = { Text(dest.label) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = startDestination,
            modifier = Modifier.padding(padding),
        ) {
            // Auth
            composable(Routes.SPLASH) {
                SplashScreen()
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(600)
                    if (com.studyinfo.app.ServiceLocator.authRepository.isSignedIn()) {
                        nav.navigate(Routes.HOME) { popUpTo(Routes.SPLASH) { inclusive = true } }
                    } else {
                        nav.navigate(Routes.LOGIN) { popUpTo(Routes.SPLASH) { inclusive = true } }
                    }
                }
            }
            composable(Routes.LOGIN) { LoginScreen(nav) }
            composable(Routes.REGISTER) { RegisterScreen(nav) }
            composable(Routes.FORGOT) { ForgotPasswordScreen(nav) }

            // Top-level
            composable(Routes.HOME) { HomeScreen(nav) }
            composable(Routes.QUESTIONS) { QuestionsHubScreen(nav) }
            composable(Routes.TODO) { TodoListScreen(nav) }
            composable(Routes.PROGRESS) { ProgressScreen(nav) }
            composable(Routes.MORE) { MoreScreen(nav) }

            // Errors
            composable(Routes.ERRORS_LIST) { ErrorListScreen(nav) }
            composable(Routes.ERROR_ADD) { ErrorEditScreen(nav, errorId = null) }
            composable(
                Routes.ERROR_DETAIL,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { entry ->
                ErrorDetailScreen(nav, errorId = entry.arguments?.getString("id").orEmpty())
            }
            composable(
                Routes.ERROR_EDIT,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { entry ->
                ErrorEditScreen(nav, errorId = entry.arguments?.getString("id"))
            }
            composable(
                Routes.ERROR_REVIEW,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { entry ->
                ErrorReviewScreen(nav, errorId = entry.arguments?.getString("id").orEmpty())
            }

            // Unsolved
            composable(Routes.UNSOLVED_LIST) { UnsolvedListScreen(nav) }
            composable(
                Routes.UNSOLVED_ADD,
                arguments = listOf(navArgument("source") { type = NavType.StringType; nullable = true; defaultValue = "" }),
            ) { entry ->
                val sourceName = entry.arguments?.getString("source").orEmpty()
                val source = if (sourceName.isBlank()) null else com.studyinfo.app.domain.model.QuestionSource.fromName(sourceName)
                UnsolvedEditScreen(nav, unsolvedId = null, initialSource = source)
            }
            composable(
                Routes.UNSOLVED_DETAIL,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { entry ->
                UnsolvedDetailScreen(nav, unsolvedId = entry.arguments?.getString("id").orEmpty())
            }
            composable(
                Routes.UNSOLVED_EDIT,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { entry ->
                UnsolvedEditScreen(nav, unsolvedId = entry.arguments?.getString("id"))
            }
            composable(Routes.RETRY_QUEUE) { RetryQueueScreen(nav) }

            // Tasks
            composable(Routes.TASK_ADD) { TaskEditScreen(nav, taskId = null) }
            composable(
                Routes.TASK_EDIT,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
            ) { entry ->
                TaskEditScreen(nav, taskId = entry.arguments?.getString("id"))
            }

            // Progress detail
            composable(
                Routes.SUBJECT_PROGRESS,
                arguments = listOf(navArgument("subject") { type = NavType.StringType }),
            ) { entry ->
                SubjectProgressScreen(nav, subjectName = entry.arguments?.getString("subject").orEmpty())
            }

            // More sub-routes
            composable(Routes.STATISTICS) { StatisticsScreen(nav) }
            composable(Routes.REVISION) { RevisionScreen(nav) }
            composable(Routes.SEARCH) { SearchScreen(nav) }
            composable(Routes.PROFILE) { ProfileScreen(nav) }
            composable(Routes.SETTINGS) { SettingsScreen(nav) }
            composable(Routes.BACKUP) { BackupScreen(nav) }
            composable(Routes.MANAGE_SOURCES) { ManageSourcesScreen(nav) }
            composable(Routes.MANAGE_CHAPTERS) { ManageChaptersScreen(nav) }
            composable(Routes.MANAGE_TAGS) { ManageTagsScreen(nav) }
            composable(Routes.ABOUT) { AboutScreen(nav) }
        }
    }
}
