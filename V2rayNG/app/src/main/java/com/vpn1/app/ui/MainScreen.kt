package com.vpn1.app.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import com.vpn1.app.R
import com.vpn1.app.model.Location
import com.vpn1.app.service.V2RayServiceManager
import com.vpn1.app.util.PreferenceHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    requestVpnPermission: (Intent) -> Unit,
    isVpnOnState: androidx.compose.runtime.MutableState<Boolean>,
    navController: NavController
) {
    val context = LocalContext.current
    var selectedLocation by remember {
        mutableStateOf(
            PreferenceHelper.getObject<Location>(context, KEY_SELECTED_LOCATION)
                ?: getAvailableLocations(context).first()
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
                tint = Color.Unspecified,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
            )
        },
        actions = {
            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { showMenuDialog = true }
                    .padding(horizontal = 24.dp, vertical = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Menu",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )

    VpnToggle(
        startVpn = { V2RayServiceManager.startVServiceFromToggle(context) },
        stopVpn = { V2RayServiceManager.stopVService(context) },
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
            navController = navController,
            onDismiss = { showMenuDialog = false }
        )
    }

    if (showLocationDialog) {
        LocationDialog(
            locations = getAvailableLocations(context),
            onOptionSelected = { country ->
                selectedLocation = getAvailableLocations(context).first { it.country == country }
            },
            onPremiumSelected = { navController.navigate("signup") },
            onDismiss = { showLocationDialog = false }
        )
    }
}