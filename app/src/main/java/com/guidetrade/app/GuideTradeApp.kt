package com.guidetrade.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.guidetrade.app.data.repository.AuthRepositoryImpl
import com.guidetrade.app.data.repository.UserRepositoryImpl
import com.guidetrade.app.data.repository.WatchlistRepositoryImpl
import com.guidetrade.app.data.repository.ResearchRepositoryImpl
import com.guidetrade.app.domain.repository.AuthRepository
import com.guidetrade.app.domain.repository.UserRepository
import com.guidetrade.app.domain.repository.WatchlistRepository
import com.guidetrade.app.domain.repository.ResearchRepository

class GuideTradeApp : Application() {

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl()
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl()
    }

    val watchlistRepository: WatchlistRepository by lazy {
        WatchlistRepositoryImpl()
    }

    val researchRepository: ResearchRepository by lazy {
        ResearchRepositoryImpl()
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    companion object {
        operator fun invoke(application: Application): GuideTradeApp =
            application as GuideTradeApp
    }
}
