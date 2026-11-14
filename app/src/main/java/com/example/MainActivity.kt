package com.example

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.FoodcourtViewModel
import com.example.ui.WebshopActionEvent
import com.example.ui.components.ComingSoonBottomSheet
import com.example.ui.screens.BrandDirectoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.WebshopScreen
import com.example.ui.theme.HayukTheme
import com.example.util.KlikitWebshopLauncher
import kotlinx.coroutines.launch

object HayukRoutes {
    const val HOME = "home"
    const val BRAND_DIRECTORY = "brand_directory"
    const val WEBSHOP_ARG_BRAND_ID = "brandId"
    const val WEBSHOP = "webshop/{$WEBSHOP_ARG_BRAND_ID}"

    fun webshopRoute(brandId: String): String = "webshop/${Uri.encode(brandId)}"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            )
        )
        setContent {
            HayukTheme {
                HayukFoodcourtApp()
            }
        }
    }
}

@Composable
fun HayukFoodcourtApp(
    viewModel: FoodcourtViewModel = viewModel()
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(viewModel, navController) {
        viewModel.events.collect { event ->
            when (event) {
                is WebshopActionEvent.OpenWebshopUrl -> {
                    navController.navigate(HayukRoutes.webshopRoute(event.brand.brandId)) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = HayukRoutes.HOME,
                enterTransition = {
                    slideInHorizontally(initialOffsetX = { it / 4 }, animationSpec = tween(240)) +
                        fadeIn(animationSpec = tween(220))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(180))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(220))
                },
                popExitTransition = {
                    slideOutHorizontally(targetOffsetX = { it / 4 }, animationSpec = tween(220)) +
                        fadeOut(animationSpec = tween(180))
                }
            ) {
                composable(route = HayukRoutes.HOME) {
                    HomeScreen(
                        uiState = uiState,
                        onOrderFeaturedBrand = { brand ->
                            viewModel.onOrderBrandClicked(brand)
                        },
                        onToggleFavorite = viewModel::toggleFavorite,
                        onNavigateToDirectory = {
                            navController.navigate(HayukRoutes.BRAND_DIRECTORY) {
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(route = HayukRoutes.BRAND_DIRECTORY) {
                    BrandDirectoryScreen(
                        uiState = uiState,
                        onSelectFilter = viewModel::selectFilter,
                        onSelectSort = viewModel::selectSort,
                        onSearchQueryChange = viewModel::updateSearchQuery,
                        onClearSearch = viewModel::clearSearch,
                        onToggleFavorite = viewModel::toggleFavorite,
                        onRefresh = viewModel::refreshBrands,
                        onOrderBrand = viewModel::onOrderBrandClicked,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    route = HayukRoutes.WEBSHOP,
                    arguments = listOf(
                        navArgument(HayukRoutes.WEBSHOP_ARG_BRAND_ID) {
                            type = NavType.StringType
                        }
                    ),
                    enterTransition = {
                        slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(220))
                    },
                    exitTransition = {
                        slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(200))
                    },
                    popEnterTransition = {
                        slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(220))
                    },
                    popExitTransition = {
                        slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(200))
                    }
                ) { backStackEntry ->
                    val brandId = backStackEntry.arguments
                        ?.getString(HayukRoutes.WEBSHOP_ARG_BRAND_ID)
                        ?.let(Uri::decode)
                        .orEmpty()
                    val brand = uiState.findBrandById(brandId)

                    if (brand != null && brand.hasValidWebshopUrl) {
                        WebshopScreen(
                            brand = brand,
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onOpenExternalBrowser = { targetUrl ->
                                val launched = KlikitWebshopLauncher.openWebshop(
                                    context = context,
                                    url = targetUrl
                                )
                                if (!launched) {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = context.getString(R.string.error_open_browser)
                                        )
                                    }
                                }
                            }
                        )
                    } else {
                        LaunchedEffect(brandId) {
                            navController.popBackStack()
                        }
                    }
                }
            }

            uiState.comingSoonBrand?.let { comingSoonBrand ->
                ComingSoonBottomSheet(
                    brand = comingSoonBrand,
                    onDismiss = viewModel::dismissComingSoonSheet
                )
            }
        }
    }
}
