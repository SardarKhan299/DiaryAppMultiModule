package com.example.diaryappmultimodule.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.diaryappmultimodule.presentation.screens.auth.AuthenticationScreen
import com.example.diaryappmultimodule.util.Constants.WRITE_SCREEN_KEY

@Composable
fun SetupNavGraph(startDestination:String,navController: NavHostController) {
    NavHost(startDestination = startDestination,navController = navController ){
        authenticationRoute()
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(){
    composable(route = Screen.Authentication.route){
        AuthenticationScreen(loadingState = false, onButtonClicked = {

        })
    }
}

fun NavGraphBuilder.homeRoute(){
    composable(route = Screen.Home.route){

    }
}

fun NavGraphBuilder.writeRoute(){
    composable(route = Screen.Write.route, arguments = listOf(navArgument(WRITE_SCREEN_KEY){
        type = NavType.StringType
        nullable = true
        defaultValue = null
    })){

    }
}