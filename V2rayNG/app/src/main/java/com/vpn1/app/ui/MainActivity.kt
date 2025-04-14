package com.vpn1.app.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vpn1.app.model.Location
import com.vpn1.app.model.UserDataResponse
import com.vpn1.app.service.V2RayServiceManager
import com.vpn1.app.service.V2RayVpnService
import com.vpn1.app.util.PreferenceHelper
import com.vpn1.app.util.VpnServiceUtil

const val KEY_SELECTED_LOCATION = "selected_location"

class MainActivity : ComponentActivity() {

    private val isVpnOnState = androidx.compose.runtime.mutableStateOf(false)
    private lateinit var vpnPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vpnPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    isVpnOnState.value = true
                    V2RayServiceManager.startVServiceFromToggle(this)
                } else {
                    isVpnOnState.value = false
                }
            }

        isVpnOnState.value = VpnServiceUtil.isVpnServiceRunning(this, V2RayVpnService::class.java)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(),
                typography = Typography()
            ) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
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