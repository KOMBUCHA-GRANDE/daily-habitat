package com.kombucha.dailyhabitat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kombucha.dailyhabitat.ui.theme.DailyHabitatTheme
import com.kombucha.oauth.GoogleLogin
import com.kombucha.oauth.KakaoLogin
import com.kombucha.oauth.Login
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val login : Login = GoogleLogin()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyHabitatTheme {
                Scaffold {
                    innerPadding ->
                    val coroutineScope = rememberCoroutineScope()
                    Button(modifier = Modifier.padding(innerPadding), onClick = {
                        coroutineScope.launch {
                            login.requestLogin(this@MainActivity)
                        }
                    })
                    {
                        Text(text = "login")
                    }
                }
            }
        }
    }
}
