package com.example.diaryappmultimodule.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.diaryappmultimodule.presentation.screens.auth.AuthViewModel
import com.example.diaryappmultimodule.presentation.screens.auth.AuthenticationScreen
import com.example.diaryappmultimodule.util.Constants.WRITE_SCREEN_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

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

        val viewModel = AuthViewModel()
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val loadingState by viewModel.loadingState

        AuthenticationScreen(authenticated = false,
            loadingState = loadingState,
            oneTapState = oneTapState,
            onButtonClicked = {
                oneTapState.open()
                viewModel.setLoading(true)
            },
            onSuccessfulFirebaseSignIn = { tokenId ->
                Log.d(NavGraphBuilder::class.simpleName, "authenticationRoute: $tokenId")
                viewModel.signInWithMongoAtlas(tokenId, onSuccess = {
                    messageBarState.addSuccess("Successfully Authenticated.")
                }, onError = {
                    messageBarState.addError(it)
                })
            },
            onFailedFirebaseSignIn = {
                Log.d(NavGraphBuilder::class.simpleName, "authenticationRoute: ${it.message}")
                messageBarState.addError(it)
                viewModel.setLoading(false)
            },
            onDialogDismissed = { message ->
                Log.d(NavGraphBuilder::class.simpleName, "authenticationRoute: ${message}")
                messageBarState.addError(Exception(message))
                viewModel.setLoading(false)
            },
            navigateToHome = {}, messageBarState = messageBarState)
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