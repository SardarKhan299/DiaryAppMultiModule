package com.example.diaryappmultimodule.navigation

import com.example.util.Screen
import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.auth.navigation.authenticationRoute
import com.example.diaryappmultimodule.presentation.components.DisplayAlertDialog
import com.example.diaryappmultimodule.presentation.screens.home.HomeScreen
import com.example.diaryappmultimodule.presentation.screens.home.HomeViewModel
import com.example.diaryappmultimodule.presentation.screens.write.WriteScreen
import com.example.util.Constants.APP_ID
import com.example.util.Constants.WRITE_SCREEN_KEY
import com.example.util.Diary
import com.example.util.RequestState
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

fun NavGraphBuilder.homeRoute(navigateToWriteScreen: () -> Unit,
                              navigateToAuth: () -> Unit,
                              onDataLoaded: ()-> Unit
                              ) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val viewModel:HomeViewModel = viewModel()
        val diaries = viewModel.diaries
        var signOutDialogOpened by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = diaries ){
            if(diaries.value !is RequestState.Loading ){
                onDataLoaded()
            }
        }

        HomeScreen(diaries = diaries.value, onMenuClicked = {
            scope.launch { drawerState.open() }
        }, navigateToWriteScren = navigateToWriteScreen, onSignedOutClicked = {
            signOutDialogOpened = true
        }, drawerState = drawerState)

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
        WriteScreen (onBackPressed = onBackPressed, onDeleteConfirmed = {}, selectedDiary = Diary().apply {
            title = "Delete Diary"
            description = "Some Random Diary.."
        })
    }
}