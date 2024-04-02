package com.example.diaryappmultimodule.presentation.screens.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(onBackPressed:()->Unit) {
    CenterAlignedTopAppBar(navigationIcon = {
        IconButton(onClick = { onBackPressed()}) {
     Icon(
         imageVector = Icons.AutoMirrored.Default.ArrowBack,
         contentDescription ="Back Arrow Icon"
     )
    }
    }, title = {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "happy",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "02-02-1998",
                style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
                textAlign = TextAlign.Center
            )
        }
    }
    )
}