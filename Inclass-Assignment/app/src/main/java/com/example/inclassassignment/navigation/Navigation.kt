package com.example.inclassassignment.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.inclassassignment.data.UserDao
import com.example.inclassassignment.screens.HomeScreen
import com.example.inclassassignment.screens.LoginScreen
import com.example.inclassassignment.screens.RegisterScreen

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home/{username}"
    const val USERNAME = "username"

    fun home(username: String): String = "home/${Uri.encode(username)}"
}

@Composable
fun AppNavigation(userDao: UserDao) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                userDao = userDao,
                onRegisterClick = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = { username ->
                    navController.navigate(Routes.home(username))
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                userDao = userDao,
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.HOME,
            arguments = listOf(navArgument(Routes.USERNAME) { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString(Routes.USERNAME).orEmpty()
            HomeScreen(
                username = username,
                onLogout = {
                    navController.popBackStack(Routes.LOGIN, false)
                }
            )
        }
    }
}

