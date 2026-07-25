package com.tabarak.quranwords.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tabarak.quranwords.ui.favorites.FavoritesScreen
import com.tabarak.quranwords.ui.flashcard.FlashcardScreen
import com.tabarak.quranwords.ui.home.HomeScreen
import com.tabarak.quranwords.ui.quiz.QuizScreen
import com.tabarak.quranwords.ui.random.RandomReviewScreen
import com.tabarak.quranwords.ui.search.SearchScreen
import com.tabarak.quranwords.ui.splash.SplashScreen
import com.tabarak.quranwords.ui.stats.StatsScreen
import com.tabarak.quranwords.ui.surah.SurahListScreen
import com.tabarak.quranwords.ui.words.WordListScreen
import java.net.URLDecoder
import java.net.URLEncoder

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val SURAHS = "surahs/{juz}"
    const val WORDS = "words/{surah}"
    const val FLASHCARD = "flashcard/{surah}"
    const val QUIZ = "quiz/{surah}"
    const val RANDOM = "random"
    const val SEARCH = "search"
    const val STATS = "stats"
    const val FAVORITES = "favorites"

    fun surahs(juz: String) = "surahs/${enc(juz)}"
    fun words(surah: String) = "words/${enc(surah)}"
    fun flashcard(surah: String) = "flashcard/${enc(surah)}"
    fun quiz(surah: String) = "quiz/${enc(surah)}"

    fun enc(s: String): String = URLEncoder.encode(s, "UTF-8")
    fun dec(s: String): String = URLDecoder.decode(s, "UTF-8")
}

@Composable
fun AppNavGraph() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(navController = navController)
        }

        composable(Routes.HOME) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Routes.SURAHS,
            arguments = listOf(navArgument("juz") { type = NavType.StringType })
        ) { backStackEntry ->
            val juz = Routes.dec(backStackEntry.arguments?.getString("juz") ?: "")
            SurahListScreen(navController = navController, juz = juz)
        }

        composable(
            route = Routes.WORDS,
            arguments = listOf(navArgument("surah") { type = NavType.StringType })
        ) { backStackEntry ->
            val surah = Routes.dec(backStackEntry.arguments?.getString("surah") ?: "")
            WordListScreen(navController = navController, surah = surah)
        }

        composable(
            route = Routes.FLASHCARD,
            arguments = listOf(navArgument("surah") { type = NavType.StringType })
        ) { backStackEntry ->
            val surah = Routes.dec(backStackEntry.arguments?.getString("surah") ?: "")
            FlashcardScreen(navController = navController, source = surah)
        }

        composable(
            route = Routes.QUIZ,
            arguments = listOf(navArgument("surah") { type = NavType.StringType })
        ) { backStackEntry ->
            val surah = Routes.dec(backStackEntry.arguments?.getString("surah") ?: "")
            QuizScreen(navController = navController, source = surah)
        }

        composable(Routes.RANDOM) {
            RandomReviewScreen(navController = navController)
        }

        composable(Routes.SEARCH) {
            SearchScreen(navController = navController)
        }

        composable(Routes.STATS) {
            StatsScreen(navController = navController)
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(navController = navController)
        }
    }
}

// معرّفات خاصة تُستخدم كمصدر بدل اسم السورة
const val SOURCE_RANDOM = "__random__"
const val SOURCE_FAVORITES = "__favorites__"
const val SOURCE_ALL = "__all__"
