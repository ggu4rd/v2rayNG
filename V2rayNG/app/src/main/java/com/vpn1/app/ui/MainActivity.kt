package com.vpn1.app.ui

import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vpn1.app.R
import com.vpn1.app.model.Location
import com.vpn1.app.service.V2RayServiceManager
import com.vpn1.app.service.V2RayVpnService
import com.vpn1.app.util.PreferenceHelper
import com.vpn1.app.util.VpnServiceUtil
import com.vpn1.app.util.getFlag

const val KEY_SELECTED_LOCATION = "selected_location"

class MainActivity : ComponentActivity() {

    private val isVpnOnState = mutableStateOf(false)
    private lateinit var vpnPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vpnPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    isVpnOnState.value = true
                    startVpn(this)
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
                MainScreen(
                    requestVpnPermission = { intent -> vpnPermissionLauncher.launch(intent) },
                    isVpnOnState = isVpnOnState
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
fun MainScreen(
    requestVpnPermission: (Intent) -> Unit,
    isVpnOnState: androidx.compose.runtime.MutableState<Boolean>
) {
    val context = LocalContext.current
    var selectedLocation by remember {
        mutableStateOf(
            PreferenceHelper.getObject<Location>(context, KEY_SELECTED_LOCATION)
                ?: freeLocations.first()
        )
    }

    LaunchedEffect(selectedLocation) {
        PreferenceHelper.saveObject(context, KEY_SELECTED_LOCATION, selectedLocation)
    }

    var showLocationDialog by remember { mutableStateOf(false) }
    var showMenuDialog by remember { mutableStateOf(false) }

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
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { showMenuDialog = true }
                    )
                    .size(24.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
    )

    VpnToggle(
        startVpn = { startVpn(context) },
        stopVpn = { stopVpn(context) },
        requestVpnPermission = requestVpnPermission,
        isVpnOnState = isVpnOnState
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LocationButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedLocation = selectedLocation,
            onClick = { showLocationDialog = true }
        )
    }

    if (showMenuDialog) {
        MenuDialog(
            onDismiss = { showMenuDialog = false }
        )
    }

    if (showLocationDialog) {
        LocationDialog(
            onOptionSelected = { country ->
                selectedLocation = freeLocations.first { it.country == country }
            },
            onDismiss = { showLocationDialog = false }
        )
    }
}

@Composable
fun VpnToggle(
    startVpn: () -> Unit,
    stopVpn: () -> Unit,
    requestVpnPermission: (Intent) -> Unit,
    isVpnOnState: androidx.compose.runtime.MutableState<Boolean>
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ToggleSwitch(
                checked = isVpnOnState.value,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        val intent = VpnService.prepare(context)
                        if (intent == null) {
                            isVpnOnState.value = true
                            startVpn()
                        } else {
                            requestVpnPermission(intent)
                            isVpnOnState.value = true
                        }
                    } else {
                        isVpnOnState.value = false
                        stopVpn()
                    }
                }
            )
            Text(
                text = if (isVpnOnState.value)
                    stringResource(id = R.string.connected)
                else
                    stringResource(id = R.string.disconnected),
                style = TextStyle(fontSize = 18.sp, color = Color(0xFF333333)),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun ToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) Color(0xFF106CD5) else Color(0xFFc4cbd3)
    )
    val thumbSize = 100.dp
    val trackWidth = 200.dp
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize else 0.dp
    )
    val interactionSource = remember { MutableInteractionSource() }
    val thumbPadding = 10.dp

    Box(
        modifier = modifier
            .width(trackWidth)
            .height(100.dp)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(50))
                .background(color = trackColor)
        )
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(thumbPadding)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
}

@Composable
fun LocationButton(
    modifier: Modifier = Modifier,
    selectedLocation: Location,
    onClick: () -> Unit = {}
) {
    val flagId = getFlag(selectedLocation.countryCode)
    Box(modifier = modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFe7eaed)),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = flagId),
                    contentDescription = "\${selectedLocation.country} Flag",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
                Text(
                    text = selectedLocation.country,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF333333),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Normal
                )
                Image(
                    painter = painterResource(id = R.drawable.chevron_right),
                    contentDescription = "Chevron Right",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}