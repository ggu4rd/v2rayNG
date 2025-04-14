package com.vpn1.app.ui

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import com.vpn1.app.R
import com.vpn1.app.api.SignUpRequest
import com.vpn1.app.api.RetrofitClient
import com.vpn1.app.model.ErrorResponse
import com.vpn1.app.util.PreferenceHelper
import okhttp3.ResponseBody
import retrofit2.Converter

@Composable
fun SignUpScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    fun handleSubmit() {
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                context,
                context.getString(R.string.username_password_required),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, R.string.enter_valid_email, Toast.LENGTH_SHORT).show()
            return
        }

        coroutineScope.launch {
            try {
                val signUpRequest = SignUpRequest(username, password, email)
                val response = RetrofitClient.apiService.signUp(signUpRequest)
                if (response.isSuccessful) {
                    response.body()?.let {
                        PreferenceHelper.saveObject(context, "USER_DATA_OBJECT", it)
                    }
                    Toast.makeText(context, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                    navController.navigate("main")
                } else {
                    val errorBody = response.errorBody()
                    val converter: Converter<ResponseBody, ErrorResponse> =
                        RetrofitClient.retrofit.responseBodyConverter(
                            ErrorResponse::class.java,
                            arrayOf()
                        )
                    val errorResponse: ErrorResponse? = errorBody?.let { converter.convert(it) }
                    val errorMsg = errorResponse?.error ?: "Unknown error"
                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Sign Up error", Toast.LENGTH_SHORT).show()
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
            text = "Sign Up",
            fontSize = 24.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(top = 24.dp)
        )
        ReusableOutlinedTextField(
            value = username,
            onValueChange = { username = it },
            labelText = "Username",
            imeAction = androidx.compose.ui.text.input.ImeAction.Next,
            onImeAction = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
        )
        ReusableOutlinedTextField(
            value = password,
            onValueChange = { password = it },
            labelText = "Password",
            imeAction = androidx.compose.ui.text.input.ImeAction.Next,
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Password,
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
            onImeAction = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
        )
        ReusableOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            labelText = "Email",
            imeAction = androidx.compose.ui.text.input.ImeAction.Done,
            keyboardType = androidx.compose.ui.text.input.KeyboardType.Email,
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
            shape = RoundedCornerShape(6.dp),
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

        Text(
            text = stringResource(R.string.have_an_account),
            color = Color(0xFF333333),
            modifier = Modifier
                .padding(top = 8.dp, bottom = 24.dp)
                .clickable {
                    navController.navigate("login")
                }
        )
    }
}