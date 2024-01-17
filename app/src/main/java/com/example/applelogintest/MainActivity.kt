package com.example.applelogintest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.applelogintest.ui.theme.AppleLoginTestTheme
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues

class MainActivity : ComponentActivity() {
    private val requestCodeAuth = 1001
    private lateinit var authService: AuthorizationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppleLoginTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

        initializeAppAuth()
        startAuth()
    }

    private fun initializeAppAuth() {
        authService = AuthorizationService(this)
    }

    private fun startAuth() {
        val serviceConfig = AuthorizationServiceConfiguration(
            Uri.parse("https://appleid.apple.com/auth/authorize"),
            Uri.parse("https://appleid.apple.com/auth/token")
        )

        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            "client_id", // replace with your client ID
            ResponseTypeValues.CODE,
            Uri.parse("redirect_uri") // replace with your redirect URI
        ).setScope("email name").setResponseMode("form_post").build()

        val authIntent = authService.getAuthorizationRequestIntent(authRequest)
        startActivityForResult(authIntent, requestCodeAuth)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == requestCodeAuth) {
            val response = AuthorizationResponse.fromIntent(data!!)
            val ex = AuthorizationException.fromIntent(data)

            response?.let {
                // Authorization was successful, handle the authorization code
                handleAuthorizationResponse(it)
            } ?: run {
                // Authorization failed, handle the error
                handleError(ex)
            }
        }
    }

    private fun handleAuthorizationResponse(response: AuthorizationResponse) {
        // Use the authorization code here to exchange for a token
        // This part is usually done on the server side for security
        val authorizationCode = response.authorizationCode
        // Send this code to your server
    }

    private fun handleError(ex: AuthorizationException?) {
        // Handle the error scenario here
        // Show an error message or take appropriate action
    }

    override fun onDestroy() {
        super.onDestroy()
        authService.dispose()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppleLoginTestTheme {
        Greeting("Android")
    }
}
