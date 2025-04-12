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
import com.vpn1.app.R
import com.vpn1.app.model.UserDataResponse
import com.vpn1.app.util.PreferenceHelper

@Composable
fun MenuDialog(
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

    val options = if (isLoggedIn) {
        listOf(logoutText, contactUsText)
    } else {
        listOf(signUpText, loginText, contactUsText)
    }

    BaseDialog(onDismiss = onDismiss) {
        Column(modifier = Modifier.padding(vertical = 18.dp)) {
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (option) {
                                signUpText -> {
                                    val intent = Intent(context, SignUpActivity::class.java)
                                    context.startActivity(intent)
                                }

                                loginText -> {
                                    val intent = Intent(context, LoginActivity::class.java)
                                    context.startActivity(intent)
                                }

                                logoutText -> {
                                    val prefs = context.getSharedPreferences(
                                        PreferenceHelper.PREFS_NAME,
                                        Context.MODE_PRIVATE
                                    )
                                    prefs.edit { remove("USER_DATA_OBJECT") }
                                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                                }

                                contactUsText -> {
                                    val browserIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        "https://1vpn.org/contact_us".toUri()
                                    )
                                    context.startActivity(browserIntent)
                                }
                            }
                            onDismiss()
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
}