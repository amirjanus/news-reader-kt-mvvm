package com.example.newsreader.di

import android.content.Context
import androidx.room.Room
import com.example.newsreader.data.MainModel
import com.example.newsreader.data.MainMvvmModel
import com.example.newsreader.data.local.interfaces.NewsLocalDb
import com.example.newsreader.data.local.room.RoNewsDao
import com.example.newsreader.data.local.room.RoomNewsDatabase
import com.example.newsreader.data.local.room.RoomNewsLocalDb
import com.example.newsreader.data.remote.newsapi.NewsApi
import com.example.newsreader.data.remote.volley.VolleyNewsApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    /**
     * Provide NewsApi interface for fetching news from remote api.
     */
    single<NewsApi> { VolleyNewsApi(androidContext()) }

    /**
     * Provide NewsLocalDb interface from accessing local database.
     */
    single<NewsLocalDb> { getRoomNewsLocalDb(androidContext()) }

    /**
     * Provide MainMvvmModel interface for accessing local and remote data sources ( repository ).
     */
    single<MainMvvmModel> { MainModel(get(), get()) }

}

/**
 * Helper function for creating main class for accessing Room database.
 */
private fun getRoomNewsLocalDb(context: Context): NewsLocalDb {
    val roNewsDao: RoNewsDao = Room.databaseBuilder(context, RoomNewsDatabase::class.java, "newsDatabase")
        .build()
        .getNewsDao()

    return RoomNewsLocalDb(roNewsDao)
}
