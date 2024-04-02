package com.example.diaryappmultimodule.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(onBackPressed:()->Unit) {
    Scaffold(topBar = {
                      WriteTopBar (onBackPressed = onBackPressed)
    }, content = {

    })
}