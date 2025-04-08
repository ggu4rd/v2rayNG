package com.vpn1.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.core.net.toUri
import com.vpn1.app.R

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(),  // or dynamicLightColorScheme(this) on Android 12+
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
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    fun handleSubmit() {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.username_password_required),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            (context as? ComponentActivity)?.finish()
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
            modifier = Modifier
                .padding(top = 24.dp)
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
            onImeAction = {
                focusManager.clearFocus()
                handleSubmit()
            }
        )

        Button(
            onClick = { handleSubmit() },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF106CD5)),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
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
            modifier = Modifier
                .padding(top = 8.dp, bottom = 24.dp),
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
                modifier = Modifier
                    .padding(horizontal = 8.dp),
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