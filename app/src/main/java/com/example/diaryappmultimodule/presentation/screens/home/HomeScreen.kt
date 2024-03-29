package com.example.diaryappmultimodule.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.diaryappmultimodule.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(drawerState: DrawerState,onMenuClicked: ()-> Unit,
               navigateToWriteScren: ()-> Unit,onSignedOutClicked: ()->Unit) {
    NavigationDrawer(drawerState =drawerState , onSignOutClicked = onSignedOutClicked) {
        Scaffold(topBar = {
            HomeScreenTopBar(onMenuClicked)
        }, floatingActionButton = {
            FloatingActionButton(onClick = navigateToWriteScren) {
                Icon(imageVector = Icons.Default.Edit ,
                    contentDescription ="New Diary Icon" )
            }
        }, content = {

        })
        
    }

}

@Composable
fun NavigationDrawer(drawerState: DrawerState,onSignOutClicked: ()->Unit,
                     content:@Composable ()-> Unit) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(content = {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp), contentAlignment = Alignment.Center){
                    Image(modifier = Modifier.size(250.dp), painter = painterResource(id = R.drawable.ic_launcher_foreground) ,
                        contentDescription = "Logo Image")
                }
                NavigationDrawerItem(label = { Row(modifier = Modifier.padding(horizontal = 12.dp)){
                    Image( painter = painterResource(id = R.drawable.google_logo) ,
                        contentDescription = "Google Image")
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "SignOut", color = MaterialTheme.colorScheme.onSurface)
                }
                }, selected = false,
                    onClick = onSignOutClicked)
            })

        },
        content = content)

}