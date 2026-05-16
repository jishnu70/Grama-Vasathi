package com.gramavasathi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.gramavasathi.R

import com.gramavasathi.ui.theme.GramaVasathiTheme
import com.gramavasathi.ui.screens.* // I'll create these
import com.gramavasathi.ui.theme.CreamWhite

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseAuth.getInstance().signInAnonymously()
        
        setContent {
            GramaVasathiTheme {
                MainScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: Int) {
    object Home : Screen("home", "Home", R.drawable.ic_home)
    object Explore : Screen("explore", "Explore", R.drawable.ic_explore)
    object Host : Screen("host", "Host", R.drawable.ic_host)
    object Guide : Screen("guide", "Guide", R.drawable.ic_guide)
    object Splash : Screen("splash", "Splash", R.drawable.ic_host)
    object Detail : Screen("detail/{homestayId}", "Detail", R.drawable.ic_explore)
    object Booking : Screen("booking/{homestayId}/{name}/{price}", "Booking", R.drawable.ic_explore)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarScreens = listOf(Screen.Home, Screen.Explore, Screen.Host, Screen.Guide)
    val shouldShowBottomBar = currentDestination?.route in bottomBarScreens.map { it.route }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar(containerColor = CreamWhite) {
                    bottomBarScreens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(ImageVector.vectorResource(id = screen.icon), contentDescription = null) },
                            label = { Text(screen.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) { SplashScreen(navController) }
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Explore.route) { ExploreScreen(navController) }
            composable(Screen.Host.route) { HostTrainingScreen(navController) }
            composable(Screen.Guide.route) { CulturalGuideScreen(navController) }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("homestayId") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("homestayId") ?: ""
                HomestayDetailScreen(id, navController)
            }
            composable(
                route = Screen.Booking.route,
                arguments = listOf(
                    navArgument("homestayId") { type = NavType.StringType },
                    navArgument("name") { type = NavType.StringType },
                    navArgument("price") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("homestayId") ?: ""
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val price = backStackEntry.arguments?.getInt("price") ?: 0
                BookingScreen(id, name, price, navController)
            }
        }
    }
}
