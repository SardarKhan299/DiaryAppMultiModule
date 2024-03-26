package com.example.diaryappmultimodule.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(onMenuClicked: ()-> Unit, navigateToWriteScren: ()-> Unit) {
    Scaffold(topBar = {
        HomeScreenTopBar(onMenuClicked)
    }, floatingActionButton = {
                              FloatingActionButton(onClick = { navigateToWriteScren }) {
                                  Icon(imageVector = Icons.Default.Edit ,
                                      contentDescription ="New Diary Icon" )
                              }
    }, content = {

    })
}