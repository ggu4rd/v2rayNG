package com.vpn1.app.ui

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.vpn1.app.R
import com.vpn1.app.model.UserDataResponse
import com.vpn1.app.util.PreferenceHelper
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MenuDialog(
    navController: NavController,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val userDataResponse: UserDataResponse? =
        PreferenceHelper.getObject(context, "USER_DATA_OBJECT")
    val isLoggedIn = userDataResponse?.sessionAuthToken?.isNotEmpty() == true

    val signUpText = stringResource(R.string.sign_up)
    val loginText = stringResource(R.string.login)
    val contactUsText = stringResource(R.string.contact_us)
    val logoutText = stringResource(R.string.logout)
    val accountText = stringResource(R.string.account)

    val options = if (isLoggedIn) {
        listOf(accountText, contactUsText, logoutText)
    } else {
        listOf(signUpText, loginText, contactUsText)
    }

    var showAccountDialog by remember { mutableStateOf(false) }

    BaseDialog(onDismiss = onDismiss) {
        Column(modifier = Modifier.padding(vertical = 18.dp)) {
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (option) {
                                signUpText -> {
                                    navController.navigate("signup")
                                    onDismiss()
                                }

                                loginText -> {
                                    navController.navigate("login")
                                    onDismiss()
                                }

                                logoutText -> {
                                    val prefs = context.getSharedPreferences(
                                        PreferenceHelper.PREFS_NAME,
                                        Context.MODE_PRIVATE
                                    )
                                    prefs.edit { remove("USER_DATA_OBJECT") }
                                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                                    onDismiss()
                                }

                                contactUsText -> {
                                    val browserIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        "https://1vpn.org/contact_us".toUri()
                                    )
                                    context.startActivity(browserIntent)
                                    onDismiss()
                                }

                                accountText -> {
                                    showAccountDialog = true
                                }
                            }
                        }
                        .padding(horizontal = 24.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = option,
                        fontSize = 18.sp,
                        color = Color(0xFF333333)
                    )
                }
            }
        }
    }

    if (showAccountDialog) {
        AccountDialog(
            userDataResponse = userDataResponse,
            onDismiss = { showAccountDialog = false }
        )
    }
}

@Composable
fun AccountDialog(
    userDataResponse: UserDataResponse?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val options = listOf(
        stringResource(R.string.username) to (userDataResponse?.username ?: "N/A"),
        stringResource(R.string.email) to (if (userDataResponse?.email.isNullOrEmpty()) "No email" else userDataResponse.email),
        stringResource(R.string.plan) to (if (userDataResponse?.isPremium == true) stringResource(R.string.premium) else stringResource(
            R.string.free
        ))
    )

    BaseDialog(onDismiss = onDismiss) {
        Column(modifier = Modifier.padding(vertical = 18.dp)) {
            options.forEach { (label, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        fontSize = 18.sp,
                        color = Color(0xFF333333),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = value,
                        fontSize = 18.sp,
                        color = Color(0xFF333333).copy(alpha = 0.6f),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.End
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            "https://1vpn.org/account".toUri()
                        )
                        context.startActivity(browserIntent)
                    }
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.edit_info),
                    fontSize = 18.sp,
                    color = Color(0xFF333333)
                )
            }
        }
    }
}