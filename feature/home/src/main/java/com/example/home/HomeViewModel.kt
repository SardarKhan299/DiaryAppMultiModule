package com.example.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mongo.Diaries
import com.example.mongo.MongoDB
import com.example.util.RequestState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    private lateinit var allDiariesJob: Job
    private lateinit var filteredDiariesJob: Job

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    init {
        observeAllDiaries()
    }

    private fun observeAllDiaries(){
        viewModelScope.launch {
            MongoDB.getAllDiaries().collect{ result->
                diaries.value = result
            }
        }

    }

}