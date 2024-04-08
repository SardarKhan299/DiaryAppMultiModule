package com.example.diaryappmultimodule.presentation.screens.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.util.Constants
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel :ViewModel() {

    var authenticated = mutableStateOf(false)
        private set

    var loadingState = mutableStateOf(false)
        private set

    fun setLoading(loading:Boolean){
        loadingState.value = loading
    }

    fun setAuthenticated(authenticated:Boolean){
        Log.d(AuthViewModel::class.simpleName, "setAuthenticated: $authenticated")
        this.authenticated.value = authenticated
    }

    fun signInWithMongoAtlas(tokenId:String,onSuccess:(Boolean)->Unit,
                             onError:(Exception)->Unit){
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO){
                    App.create(Constants.APP_ID).login(
                        // to login with JWT Authentication...//
                        Credentials.jwt(tokenId)
                        //Credentials.google(tokenId,GoogleAuthType.ID_TOKEN)
                    ).loggedIn
                }
                withContext(Dispatchers.Main){
                    onSuccess(result)
                }
                Log.d(AuthViewModel::class.simpleName, "signInWithMongoAtlas: Success...${result}")

            }catch (e:Exception){
                Log.d(AuthViewModel::class.simpleName, "signInWithMongoAtlas: ${e.message}")
                withContext(Dispatchers.Main){
                    onError(e)
                }
            }
        }

    }



}