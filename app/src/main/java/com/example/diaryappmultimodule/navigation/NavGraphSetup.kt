package com.example.diaryappmultimodule.navigation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.diaryappmultimodule.presentation.screens.auth.AuthViewModel
import com.example.diaryappmultimodule.presentation.screens.auth.AuthenticationScreen
import com.example.diaryappmultimodule.util.Constants.APP_ID
import com.example.diaryappmultimodule.util.Constants.WRITE_SCREEN_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.launch

@Composable
fun SetupNavGraph(startDestination:String,navController: NavHostController) {
    NavHost(startDestination = startDestination,navController = navController ){
        authenticationRoute(navigateToHome = {
            Log.d(NavGraphBuilder::class.simpleName, "SetupNavGraph: Goto Home...")
            navController.popBackStack()
            navController.navigate(Screen.Home.route)
        })
        homeRoute()
        writeRoute()
    }
}

fun NavGraphBuilder.authenticationRoute(navigateToHome:()->Unit){
    composable(route = Screen.Authentication.route){

        val viewModel: AuthViewModel = viewModel()
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val loadingState by viewModel.loadingState
        val authenticated by viewModel.authenticated

        AuthenticationScreen(authenticated = authenticated,
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
                    viewModel.setLoading(false)
                    viewModel.setAuthenticated(true)
                }, onError = {
                    messageBarState.addError(it)
                    viewModel.setLoading(false)
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
            navigateToHome = navigateToHome, messageBarState = messageBarState)
    }
}

fun NavGraphBuilder.homeRoute(){
    composable(route = Screen.Home.route){
        val scope = rememberCoroutineScope()
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Button(onClick = { scope.launch {  App.create(APP_ID).currentUser?.logOut() }}) {
                Text(text = "Logout")
            }
        }
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