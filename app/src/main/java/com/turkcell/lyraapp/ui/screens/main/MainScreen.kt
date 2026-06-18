package com.turkcell.lyraapp.ui.screens.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.turkcell.lyraapp.ui.components.LyraBnb
import com.turkcell.lyraapp.ui.navigation.BottomNavDestination
import com.turkcell.lyraapp.ui.screens.createplaylist.CreatePlaylistRoute
import com.turkcell.lyraapp.ui.screens.favorites.FavoritesRoute
import com.turkcell.lyraapp.ui.screens.home.HomeRoute
import com.turkcell.lyraapp.ui.screens.library.LibraryRoute
import com.turkcell.lyraapp.ui.screens.playlistdetail.PlaylistDetailRoute
import com.turkcell.lyraapp.ui.screens.profile.ProfileRoute
import com.turkcell.lyraapp.ui.screens.search.SearchRoute

private const val PLAYLIST_DETAIL_ROUTE = "playlist_detail"
private const val CREATE_PLAYLIST_ROUTE = "create_playlist"

@Composable
fun MainRoute() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val currentDestination = BottomNavDestination.fromRoute(currentRoute)
    val showBottomBar = BottomNavDestination.entries.any { it.route == currentRoute }

    MainScreen(
        currentDestination = currentDestination,
        showBottomBar = showBottomBar,
        onDestinationSelected = { destination ->
            navController.navigate(destination.route) {
                popUpTo(BottomNavDestination.HOME.route) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = BottomNavDestination.HOME.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(BottomNavDestination.HOME.route) { HomeRoute() }
            composable(BottomNavDestination.SEARCH.route) { SearchRoute() }
            composable(BottomNavDestination.LIBRARY.route) {
                LibraryRoute(
                    onNavigateToPlaylist = { navController.navigate(PLAYLIST_DETAIL_ROUTE) },
                    onNavigateToCreatePlaylist = { navController.navigate(CREATE_PLAYLIST_ROUTE) },
                )
            }
            composable(BottomNavDestination.FAVORITES.route) { FavoritesRoute() }
            composable(BottomNavDestination.PROFILE.route) { ProfileRoute() }
            composable(PLAYLIST_DETAIL_ROUTE) {
                PlaylistDetailRoute(
                    onNavigateBack = { navController.navigateUp() },
                )
            }
            composable(CREATE_PLAYLIST_ROUTE) {
                CreatePlaylistRoute(
                    onNavigateBack = { navController.navigateUp() },
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    currentDestination: BottomNavDestination,
    showBottomBar: Boolean,
    onDestinationSelected: (BottomNavDestination) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (showBottomBar) {
                LyraBnb(
                    currentDestination = currentDestination,
                    onDestinationSelected = onDestinationSelected,
                )
            }
        },
        content = content,
    )
}
