package com.example.write.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.util.Constants
import com.example.util.Diary
import com.example.util.Screen
import com.example.write.WriteScreen

fun NavGraphBuilder.writeRoute(onBackPressed:()->Unit) {
    composable(route = Screen.Write.route, arguments = listOf(navArgument(Constants.WRITE_SCREEN_KEY) {
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