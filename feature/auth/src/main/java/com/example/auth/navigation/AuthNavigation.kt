package com.example.auth.navigation

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.auth.AuthViewModel
import com.example.auth.AuthenticationScreen
import com.example.util.Screen
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.rememberOneTapSignInState

    fun NavGraphBuilder.authenticationRoute(navigateToHome: () -> Unit, onDataLoaded: ()-> Unit) {
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
                    android.util.Log.d(NavGraphBuilder::class.simpleName, "authenticationRoute: $tokenId")
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
                    android.util.Log.d(NavGraphBuilder::class.simpleName, "authenticationRoute: ${it.message}")
                    messageBarState.addError(it)
                    viewModel.setLoading(false)
                },
                onDialogDismissed = { message ->
                    android.util.Log.d(NavGraphBuilder::class.simpleName, "authenticationRoute: ${message}")
                    messageBarState.addError(Exception(message))
                    viewModel.setLoading(false)
                },
                navigateToHome = navigateToHome, messageBarState = messageBarState
            )
        }
    }