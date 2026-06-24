package com.turkcell.lyraapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.turkcell.lyraapp.ui.screens.completeprofile.CompleteProfileRoute
import com.turkcell.lyraapp.ui.screens.main.MainRoute
import com.turkcell.lyraapp.ui.screens.otp.OtpVerifyRoute
import com.turkcell.lyraapp.ui.screens.phone.PhoneEntryRoute

private object Routes {
    const val PHONE_ENTRY = "phone_entry"
    const val OTP_VERIFY = "otp_verify/{phone}"
    const val COMPLETE_PROFILE = "complete_profile"
    const val MAIN = "main"

    fun otpVerify(phone: String) = "otp_verify/$phone"
}

@Composable
fun LyraNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.PHONE_ENTRY,
        modifier = modifier,
    ) {
        composable(Routes.PHONE_ENTRY) {
            PhoneEntryRoute(
                onNavigateToOtpVerify = { phone ->
                    navController.navigate(Routes.otpVerify(phone))
                },
            )
        }

        composable(
            route = Routes.OTP_VERIFY,
            arguments = listOf(navArgument("phone") { type = NavType.StringType }),
        ) {
            OtpVerifyRoute(
                onNavigateToCompleteProfile = {
                    navController.navigate(Routes.COMPLETE_PROFILE)
                },
                onNavigateToHome = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.PHONE_ENTRY) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.navigateUp() },
            )
        }

        composable(Routes.COMPLETE_PROFILE) {
            CompleteProfileRoute(
                onNavigateToHome = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.PHONE_ENTRY) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.navigateUp() },
            )
        }

        composable(Routes.MAIN) {
            MainRoute()
        }
    }
}
