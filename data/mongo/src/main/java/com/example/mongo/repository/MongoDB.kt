package com.example.mongo

import android.util.Log
import com.example.util.Constants.APP_ID
import com.example.util.Diary
import com.example.util.RequestState
import com.example.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.subscriptions
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.ZoneId

object MongoDB :MongoRepository {

    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm: Realm

    init {
        configureRealm()
    }
    override fun configureRealm() {
        if (user != null) {
            Log.d(MongoDB::class.simpleName, "configureRealm: ${user.id}")
            val config = SyncConfiguration.Builder(user, setOf(Diary::class))
                .initialSubscriptions { sub ->
                    add(
                        query = sub.query<Diary>("ownerId == $0", user.id),
                        name = "User's Diaries"
                    )
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
            // Refreshing subscriptions after configuring the Realm instance

            realm.subscriptions.refresh()
        }

    }

    override fun getAllDiaries(): Flow<Diaries> {
       return if(user!=null){
            try {
                Log.d(MongoDB::class.simpleName, "getAllDiaries:")
                realm.query<Diary>(query = "ownerId == $0", user.id)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map {result->
                        RequestState.Success(data = result.list.groupBy {
                            it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                        })
                    }
            }catch (e:Exception){
                flow { emit(RequestState.Error(e)) }
            }
        }else{
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }
}

private class UserNotAuthenticatedException : Exception ("User is not LoggedIn...")