package com.vpn1.app.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vpn1.app.api.RetrofitClient
import com.vpn1.app.model.Location
import com.vpn1.app.model.UserDataResponse
import com.vpn1.app.service.V2RayServiceManager
import com.vpn1.app.service.V2RayVpnService
import com.vpn1.app.util.PreferenceHelper
import com.vpn1.app.util.VpnServiceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val KEY_SELECTED_LOCATION = "selected_location"

@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    private val isVpnOnState = mutableStateOf(false)
    private lateinit var vpnPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vpnPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                isVpnOnState.value = true
                V2RayServiceManager.startVServiceFromToggle(this)
            } else {
                isVpnOnState.value = false
            }
        }

        isVpnOnState.value = VpnServiceUtil.isVpnServiceRunning(this, V2RayVpnService::class.java)

        fetchUserData(this)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(),
                typography = Typography()
            ) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "main",
                    enterTransition = {
                        slideInHorizontally { it }
                    },
                    exitTransition = {
                        slideOutHorizontally { -it }
                    },
                    popEnterTransition = {
                        slideInHorizontally { -it }
                    },
                    popExitTransition = {
                        slideOutHorizontally { it }
                    }
                ) {
                    composable("main") {
                        MainScreen(
                            requestVpnPermission = { intent -> vpnPermissionLauncher.launch(intent) },
                            isVpnOnState = isVpnOnState,
                            navController = navController
                        )
                    }
                    composable("login") {
                        LoginScreen(navController = navController)
                    }
                    composable("signUp") {
                        SignUpScreen(navController = navController)
                    }
                }
            }
        }
    }
}

fun getAvailableLocations(context: Context): List<Location> {
    val userDataResponse = PreferenceHelper.getObject<UserDataResponse>(context, "USER_DATA_OBJECT")
    return if (userDataResponse != null && userDataResponse.isPremium && userDataResponse.locations.isNotEmpty()) {
        userDataResponse.locations
    } else {
        freeLocations
    }
}

fun fetchUserData(context: Context) {
    val userDataResponse: UserDataResponse? =
        PreferenceHelper.getObject(context, "USER_DATA_OBJECT")

    val sessionAuthToken = userDataResponse?.sessionAuthToken

    if (sessionAuthToken != null) {
        if (context is LifecycleOwner) {
            context.lifecycleScope.launch {
                try {
                    val headers = mapOf(
                        "Authorization" to "Token $sessionAuthToken"
                    )

                    val response = RetrofitClient.apiService.fetchUserData(headers)

                    if (response.isSuccessful) {
                        val userData = response.body()
                        userData?.let {
                            PreferenceHelper.saveObject(context, "USER_DATA_OBJECT", it)
                        }

                        refreshToken(context)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("Error: $errorBody")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}

fun refreshToken(context: Context) {
    if (context is LifecycleOwner) {
        context.lifecycleScope.launch {
            try {
//                val lastTokenRefresh: Long =
//                    PreferenceHelper.getObject(context, "LAST_TOKEN_REFRESH") ?: 0L
//                val now = System.currentTimeMillis()

//                if (lastTokenRefresh == 0L || now - lastTokenRefresh > 24 * 60 * 60 * 1000) {
                val userDataResponse: UserDataResponse? =
                    PreferenceHelper.getObject(context, "USER_DATA_OBJECT")
                val sessionAuthToken = userDataResponse?.sessionAuthToken

                if (!sessionAuthToken.isNullOrEmpty()) {
                    val response = withContext(Dispatchers.IO) {
                        RetrofitClient.apiService.refreshToken(
                            headers = mapOf(
                                "Content-Type" to "application/json",
                                "Authorization" to "Token $sessionAuthToken"
                            )
                        )
                    }

                    if (response.isSuccessful) {
                        val refreshTokenResponse = response.body()
                        refreshTokenResponse?.sessionAuthToken?.let { newToken ->
                            val updatedUserDataResponse = userDataResponse.copy(
                                sessionAuthToken = newToken
                            )
                            PreferenceHelper.saveObject(
                                context,
                                "USER_DATA_OBJECT",
                                updatedUserDataResponse
                            )
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("Error: $errorBody")
                    }
                }

//                    PreferenceHelper.saveObject(context, "LAST_TOKEN_REFRESH", now)
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}