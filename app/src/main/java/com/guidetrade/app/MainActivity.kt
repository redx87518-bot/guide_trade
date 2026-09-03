package com.guidetrade.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.guidetrade.app.data.repository.AuthRepositoryImpl
import com.guidetrade.app.domain.model.User
import com.guidetrade.app.ui.navigation.GuideTradeAppNavHost
import com.guidetrade.app.ui.screens.auth.AuthScreen
import com.guidetrade.app.ui.theme.GuideTradeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideTradeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authRepository = remember { AuthRepositoryImpl() }
                    val user by authRepository.currentUser.collectAsState(initial = null)

                    if (user != null) {
                        GuideTradeAppNavHost(
                            onSignOut = { authRepository.signOut() }
                        )
                    } else {
                        AuthScreen(onSignedIn = { })
                    }
                }
            }
        }
    }
}
