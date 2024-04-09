package com.example.diaryappmultimodule.navigation

import com.example.util.Screen
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.auth.navigation.authenticationRoute
import com.example.home.navigation.homeRoute
import com.example.write.navigation.writeRoute

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



