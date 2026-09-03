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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.guidetrade.app.domain.model.User
import com.guidetrade.app.ui.navigation.GuideTradeAppNavHost
import com.guidetrade.app.ui.screens.auth.AuthScreen
import com.guidetrade.app.ui.theme.GuideTradeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideTradeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val app = application as GuideTradeApp
                    val authRepository = app.authRepository
                    val user by authRepository.currentUser.collectAsState(initial = null)

                    if (user != null) {
                        GuideTradeAppNavHost(
                            onSignOut = {
                                lifecycleScope.launch {
                                    authRepository.signOut()
                                }
                            }
                        )
                    } else {
                        AuthScreen(onSignedIn = { })
                    }
                }
            }
        }
    }
}
