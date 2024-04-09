package com.example.home.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.home.HomeScreen
import com.example.home.HomeViewModel
import com.example.ui.theme.DisplayAlertDialog
import com.example.util.Constants
import com.example.util.RequestState
import com.example.util.Screen
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun NavGraphBuilder.homeRoute(navigateToWriteScreen: () -> Unit,
                              navigateToAuth: () -> Unit,
                              onDataLoaded: ()-> Unit
) {
    composable(route = Screen.Home.route) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val viewModel: HomeViewModel = viewModel()
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
                val user = App.create(Constants.APP_ID).currentUser
                if (user != null) {
                    user.logOut()
                    withContext(Dispatchers.Main) {
                        navigateToAuth()
                    }
                }
            }})


    }
}