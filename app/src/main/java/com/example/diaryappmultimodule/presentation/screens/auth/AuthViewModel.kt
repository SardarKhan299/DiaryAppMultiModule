package com.example.diaryappmultimodule.presentation.screens.auth

import android.app.appsearch.AppSearchManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diaryappmultimodule.util.Constants
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.GoogleAuthType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel :ViewModel() {

    var loadingState = mutableStateOf(false)
        private set

    fun setLoading(loading:Boolean){
        loadingState.value = loading
    }

    fun signInWithMongoAtlas(tokenId:String,onSuccess:(Boolean)->Unit,
                             onError:(Exception)->Unit){
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO){
                    App.create(Constants.APP_ID).login(
                        Credentials.google(tokenId,GoogleAuthType.ID_TOKEN)
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