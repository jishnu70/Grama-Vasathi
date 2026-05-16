package com.gramavasathi.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gramavasathi.ui.booking.BookingScreen
import com.gramavasathi.ui.cultural.CulturalGuideScreen
import com.gramavasathi.ui.detail.HomestayDetailScreen
import com.gramavasathi.ui.explore.ExploreScreen
import com.gramavasathi.ui.explore.MapScreen
import com.gramavasathi.ui.home.HomeScreen
import com.gramavasathi.ui.host.HostTrainingScreen
import com.gramavasathi.ui.splash.SplashScreen

private data class NavItem(val route: String, val label: String, val icon: @Composable () -> Unit)

@Composable
fun GramaVasathiRoot() {
    val navController = rememberNavController()
    val items = listOf(
        NavItem("home", "Home") { Icon(Icons.Rounded.Home, null) },
        NavItem("explore", "Explore") { Icon(Icons.Rounded.Search, null) },
        NavItem("host", "Host") { Icon(Icons.Rounded.Spa, null) },
        NavItem("guide", "Guide") { Icon(Icons.Rounded.Book, null) }
    )
    val entry by navController.currentBackStackEntryAsState()
    val current = entry?.destination
    val showBottom = current?.route in listOf("home", "explore", "host", "guide")

    Scaffold(
        bottomBar = {
            if (showBottom) {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = current?.hierarchy?.any { it.route == item.route } == true,
                            onClick = { navController.navigate(item.route) { launchSingleTop = true } },
                            icon = item.icon,
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { pad ->
        NavHost(navController, startDestination = "splash", modifier = Modifier.padding(pad)) {
            composable("splash") { SplashScreen { navController.navigate("home") { popUpTo("splash") { inclusive = true } } } }
            composable("home") { HomeScreen(onExplore = { navController.navigate("explore") }, onHost = { navController.navigate("host") }, onGuide = { navController.navigate("guide") }, onDetails = { navController.navigate("detail/$it") }) }
            composable("explore") { ExploreScreen(onDetails = { navController.navigate("detail/$it") }, onMap = { navController.navigate("map") }) }
            composable("map") { MapScreen(onBack = { navController.popBackStack() }, onDetails = { navController.navigate("detail/$it") }) }
            composable("host") { HostTrainingScreen() }
            composable("guide") { CulturalGuideScreen() }
            composable("detail/{id}", arguments = listOf(navArgument("id") { type = NavType.StringType })) {
                HomestayDetailScreen(id = it.arguments?.getString("id").orEmpty(), onBook = { hid -> navController.navigate("booking/$hid") })
            }
            composable("booking/{id}", arguments = listOf(navArgument("id") { type = NavType.StringType })) {
                BookingScreen(id = it.arguments?.getString("id").orEmpty(), onDone = { navController.navigate("home") { popUpTo("home") { inclusive = true } } })
            }
        }
    }
}
