package com.example.diaryappmultimodule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.diaryappmultimodule.data.repository.MongoDB
import com.example.diaryappmultimodule.navigation.Screen
import com.example.diaryappmultimodule.navigation.SetupNavGraph
import com.example.diaryappmultimodule.ui.theme.DiaryAppMultiModuleTheme
import com.example.diaryappmultimodule.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window,false)
        installSplashScreen()
        setContent {
            DiaryAppMultiModuleTheme {
                val navController = rememberNavController()
                SetupNavGraph(getStartDestination(),navController)
            }
        }
    }

    private fun getStartDestination():String{
        val user = App.create(APP_ID).currentUser
        return if (user!=null && user.loggedIn) Screen.Home.route
        else Screen.Authentication.route

    }
}
