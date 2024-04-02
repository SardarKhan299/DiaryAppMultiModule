package com.example.diaryappmultimodule.navigation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.diaryappmultimodule.data.repository.MongoDB
import com.example.diaryappmultimodule.model.RequestState
import com.example.diaryappmultimodule.presentation.components.DisplayAlertDialog
import com.example.diaryappmultimodule.presentation.screens.auth.AuthViewModel
import com.example.diaryappmultimodule.presentation.screens.auth.AuthenticationScreen
import com.example.diaryappmultimodule.presentation.screens.home.HomeScreen
import com.example.diaryappmultimodule.presentation.screens.home.HomeViewModel
import com.example.diaryappmultimodule.presentation.screens.write.WriteScreen
import com.example.diaryappmultimodule.util.Constants.APP_ID
import com.example.diaryappmultimodule.util.Constants.WRITE_SCREEN_KEY
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

fun NavGraphBuilder.authenticationRoute(navigateToHome: () -> Unit,onDataLoaded: ()-> Unit) {
    composable(route = Screen.Authentication.route) {

        val viewModel: AuthViewModel = viewModel()
        val oneTapState = rememberOneTapSignInState()
        val messageBarState = rememberMessageBarState()
        val loadingState by viewModel.loadingState
        val authenticated by viewModel.authenticated

        LaunchedEffect(key1 = Unit){
            onDataLoaded()
        }

        AuthenticationScreen(
            authenticated = authenticated,
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
            navigateToHome = navigateToHome, messageBarState = messageBarState
        )
    }
}

fun NavGraphBuilder.homeRoute(navigateToWriteScreen: () -> Unit,
                              navigateToAuth: () -> Unit,
                              onDataLoaded: ()-> Unit
                              ) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val viewModel:HomeViewModel = viewModel()
        val diaries by viewModel.diaries
        var signOutDialogOpened by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = diaries ){
            if(diaries !is RequestState.Loading ){
                onDataLoaded()
            }
        }

        HomeScreen(diaries = diaries, onMenuClicked = {
            scope.launch { drawerState.open() }
        }, navigateToWriteScren = navigateToWriteScreen, onSignedOutClicked = {
            signOutDialogOpened = true
        }, drawerState = drawerState)
        
        LaunchedEffect(key1 = Unit){
            MongoDB.configureRealm()
        }

        DisplayAlertDialog(
            title = "Sign Out",
            message = "Are You sure you want to SignOut ?",
            dialogOpened = signOutDialogOpened,
            onDialogClosed = {signOutDialogOpened = false},
            onYesClicked = {scope.launch(Dispatchers.IO) {
                val user = App.create(APP_ID).currentUser
                if (user != null) {
                    user.logOut()
                    withContext(Dispatchers.Main) {
                        navigateToAuth()
                    }
                }
            }})


    }
}

fun NavGraphBuilder.writeRoute(onBackPressed:()->Unit) {
    composable(route = Screen.Write.route, arguments = listOf(navArgument(WRITE_SCREEN_KEY) {
        type = NavType.StringType
        nullable = true
        defaultValue = null
    })) {
        WriteScreen (onBackPressed = onBackPressed)
    }
}