package com.vpn1.app.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.vpn1.app.R

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(),
                typography = Typography()
            ) {
                SignUpScreen()
            }
        }
    }
}

@Composable
fun SignUpScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    fun handleLogin() {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.username_password_required),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            (context as? ComponentActivity)?.finish()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = stringResource(R.string.sign_up),
            fontSize = 24.sp,
            color = Color(0xFF333333)
        )

        ReusableOutlinedTextField(
            value = username,
            onValueChange = { username = it },
            labelText = stringResource(R.string.username),
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
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
                handleLogin()
            }
        )

        ReusableOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            labelText = stringResource(R.string.email),
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
        )

        Button(
            onClick = { handleLogin() },
            modifier = Modifier
                .fillMaxWidth()
                .height(62.dp)
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF106CD5)),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                letterSpacing = 0.sp
            )
        }
    }
}