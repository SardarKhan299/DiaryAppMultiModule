package com.example.diaryappmultimodule.navigation

import com.example.util.Screen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.auth.navigation.authenticationRoute
import com.example.diaryappmultimodule.presentation.screens.write.WriteScreen
import com.example.home.navigation.homeRoute
import com.example.util.Constants.WRITE_SCREEN_KEY
import com.example.util.Diary

@Composable
fun SetupNavGraph(startDestination: String, navController: NavHostController,onDataLoaded: () -> Unit) {
    NavHost(startDestination = startDestination, navController = navController) {
        authenticationRoute(navigateToHome = {
            Log.d(NavGraphBuilder::class.simpleName, "SetupNavGraph: Goto Home...")
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        },onDataLoaded = onDataLoaded)
        homeRoute(navigateToWriteScreen = {
            navController.navigate(Screen.Write.route)
        }, navigateToAuth = {
            navController.popBackStack()
            navController.navigate(Screen.Authentication.route)
        },onDataLoaded = onDataLoaded)
        writeRoute(onBackPressed = {
            navController.popBackStack()
        })
    }
}



fun NavGraphBuilder.writeRoute(onBackPressed:()->Unit) {
    composable(route = Screen.Write.route, arguments = listOf(navArgument(WRITE_SCREEN_KEY) {
        type = NavType.StringType
        nullable = true
        defaultValue = null
    })) {
        WriteScreen (onBackPressed = onBackPressed, onDeleteConfirmed = {}, selectedDiary = Diary().apply {
            title = "Delete Diary"
            description = "Some Random Diary.."
        })
    }
}