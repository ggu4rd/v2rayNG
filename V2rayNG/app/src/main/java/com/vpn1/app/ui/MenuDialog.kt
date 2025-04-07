package com.vpn1.app.ui

import android.content.Intent
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

@Composable
fun MenuDialog(
    onDismiss: () -> Unit
) {
    val options = listOf("Sign Up", "Login", "Contact Us")
    val context = LocalContext.current

    BaseDialog(onDismiss = onDismiss) {
        Column(modifier = Modifier.padding(vertical = 18.dp)) {
            options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            when (option) {
                                "Login" -> {
                                    val intent = Intent(context, LoginActivity::class.java)
                                    context.startActivity(intent)
                                }

                                "Contact Us" -> {
                                    val browserIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        "https://1vpn.org/contact_us".toUri()
                                    )
                                    context.startActivity(browserIntent)
                                }

                                "Sign Up" -> {
                                    // Handle sign up action here
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