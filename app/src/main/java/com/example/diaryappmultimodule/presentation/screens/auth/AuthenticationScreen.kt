package com.example.diaryappmultimodule.presentation.screens.auth

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.diaryappmultimodule.util.Constants.CLIENT_ID
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(authenticated: Boolean,
                         loadingState: Boolean,
                         oneTapState: OneTapSignInState,
                         onButtonClicked: () -> Unit,
                         onSuccessfulFirebaseSignIn: (String) -> Unit,
                         onFailedFirebaseSignIn: (Exception) -> Unit,
                         onDialogDismissed: (String) -> Unit,
                         navigateToHome: () -> Unit) {
    Scaffold( content = {
        AuthenticationContent(loadingState = loadingState, onButtonClicked =onButtonClicked )
    })

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            val credential = GoogleAuthProvider.getCredential(tokenId, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        onSuccessfulFirebaseSignIn(tokenId)
                    } else {
                        task.exception?.let { onFailedFirebaseSignIn(it) }
                    }
                }
        },
        onDialogDismissed = { message ->
            onDialogDismissed(message)
        }
    )

    LaunchedEffect(key1 = authenticated) {
        if (authenticated) {
            navigateToHome()
        }
    }
}