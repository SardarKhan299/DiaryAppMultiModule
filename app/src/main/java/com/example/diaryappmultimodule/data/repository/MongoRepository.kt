package com.example.diaryappmultimodule.data.repository

import com.example.diaryappmultimodule.model.Diary
import com.example.diaryappmultimodule.model.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>
interface MongoRepository {
    fun configureRealm()
    fun getAllDiaries(): Flow<Diaries>
}