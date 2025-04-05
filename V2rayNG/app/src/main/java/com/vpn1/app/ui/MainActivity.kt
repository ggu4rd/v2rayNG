package com.vpn1.app.ui

import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vpn1.app.R
import com.vpn1.app.service.V2RayServiceManager

class MainActivity : ComponentActivity() {

    private lateinit var vpnPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vpnPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    startVpn(this)
                }
            }

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(),
                typography = Typography()
            ) {
                MainScreen(
                    requestVpnPermission = { intent -> vpnPermissionLauncher.launch(intent) }
                )
            }
        }
    }
}

private fun startVpn(context: Context) {
    V2RayServiceManager.startVServiceFromToggle(context)
}

private fun stopVpn(context: Context) {
    V2RayServiceManager.stopVService(context)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(requestVpnPermission: (Intent) -> Unit) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(24.dp)) {
        TopAppBar(
            title = {},
            navigationIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    tint = Color.Unspecified
                )
            },
            actions = {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Menu",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .clickable { /* Handle menu click */ }
                        .size(24.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            ),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        VpnToggle(
            startVpn = { startVpn(context) },
            stopVpn = { stopVpn(context) },
            requestVpnPermission = requestVpnPermission,
        )
    }
}

@Composable
fun VpnToggle(
    startVpn: () -> Unit,
    stopVpn: () -> Unit,
    requestVpnPermission: (Intent) -> Unit
) {
    var isVpnOn by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(
            checked = isVpnOn,
            onCheckedChange = { isChecked ->
                isVpnOn = isChecked
                if (isChecked) {
                    val intent = VpnService.prepare(context)
                    if (intent == null) {
                        startVpn()
                    } else {
                        requestVpnPermission(intent)
                    }
                } else {
                    stopVpn()
                }
            }
        )
        Text(
            text = if (isVpnOn) "VPN On" else "VPN Off",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}