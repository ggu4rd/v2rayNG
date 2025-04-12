package com.vpn1.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusDirection
import androidx.core.net.toUri
import com.google.gson.Gson
import com.vpn1.app.R
import com.vpn1.app.api.LoginRequest
import com.vpn1.app.api.RetrofitClient
import com.vpn1.app.model.ErrorResponse
import com.vpn1.app.model.UserDataResponse
import com.vpn1.app.util.PreferenceHelper
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(),
                typography = Typography()
            ) {
                LoginScreen()
            }
        }
    }
}

@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var showTokenInput by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val usernamePasswordRequiredMsg = stringResource(R.string.username_password_required)

    fun handleSubmit() {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, usernamePasswordRequiredMsg, Toast.LENGTH_SHORT).show()
        } else {
            coroutineScope.launch {
                try {
                    val loginRequest = if (token.isNotEmpty()) {
                        LoginRequest(username, password, token)
                    } else {
                        LoginRequest(username, password)
                    }
                    val response = RetrofitClient.apiService.login(loginRequest)
                    if (response.isSuccessful) {
                        val rawJson = response.body()?.string()
                        val gson = Gson()
                        val userDataResponse =
                            rawJson?.let { gson.fromJson(it, UserDataResponse::class.java) }
                        userDataResponse?.let {
                            PreferenceHelper.saveObject(
                                context,
                                "USER_DATA_OBJECT",
                                it
                            )
                        }
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        (context as? ComponentActivity)?.finish()
                    } else {
                        val errorString = response.errorBody()?.string() ?: "Unknown error"
                        if (errorString.contains("1001") && !showTokenInput) {
                            showTokenInput = true
                        } else {
                            try {
                                val errorResponse =
                                    Gson().fromJson(errorString, ErrorResponse::class.java)
                                Toast.makeText(context, errorResponse.error, Toast.LENGTH_SHORT)
                                    .show()
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Login error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = stringResource(R.string.login),
            fontSize = 24.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(top = 24.dp)
        )

        ReusableOutlinedTextField(
            value = username,
            onValueChange = { username = it },
            labelText = stringResource(R.string.username),
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
        )

        ReusableOutlinedTextField(
            value = password,
            onValueChange = { password = it },
            labelText = stringResource(R.string.password),
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    handleSubmit()
                }
            )
        )

        if (showTokenInput) {
            ReusableOutlinedTextField(
                value = token,
                onValueChange = { token = it },
                labelText = stringResource(R.string.twofa_token),
                imeAction = ImeAction.Done,
                onImeAction = {
                    focusManager.clearFocus()
                    handleSubmit()
                }
            )
        }

        Button(
            onClick = { handleSubmit() },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF106CD5)),
            shape = RoundedCornerShape(6.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = stringResource(R.string.login),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                letterSpacing = 0.sp
            )
        }

        Row(
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.create_account),
                color = Color(0xFF333333),
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, SignUpActivity::class.java))
                }
            )
            Text(
                text = "|",
                color = Color(0xFF333333),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = stringResource(R.string.forgot_password),
                color = Color(0xFF333333),
                modifier = Modifier.clickable {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        "https://1vpn.org/password_reset_request".toUri()
                    )
                    context.startActivity(browserIntent)
                }
            )
        }
    }
}