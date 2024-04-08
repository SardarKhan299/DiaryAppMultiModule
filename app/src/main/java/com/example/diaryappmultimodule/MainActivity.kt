package com.example.diaryappmultimodule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.util.Screen
import com.example.diaryappmultimodule.navigation.SetupNavGraph
import com.example.ui.theme.DiaryAppMultiModuleTheme
import com.example.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {

    var keepSplashOpen = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false)
        installSplashScreen().setKeepOnScreenCondition{
            keepSplashOpen
        }
        setContent {
            DiaryAppMultiModuleTheme {
                val navController = rememberNavController()
                SetupNavGraph(getStartDestination(),navController, onDataLoaded = {
                    keepSplashOpen = false
                })
            }
        }
    }

    private fun getStartDestination():String{
        val user = App.create(APP_ID).currentUser
        return if (user!=null && user.loggedIn) Screen.Home.route
        else Screen.Authentication.route

    }
}
