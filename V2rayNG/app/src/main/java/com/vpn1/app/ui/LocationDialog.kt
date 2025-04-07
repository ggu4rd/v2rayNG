package com.vpn1.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vpn1.app.util.getFlag

@Composable
fun LocationDialog(
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    BaseDialog(onDismiss = onDismiss) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(vertical = 18.dp)
        ) {
            freeLocations.forEach { location ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onOptionSelected(location.country)
                            onDismiss()
                        }
                        .padding(horizontal = 24.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val flagId = getFlag(location.countryCode)
                    Image(
                        painter = painterResource(id = flagId),
                        contentDescription = "${location.country} Flag",
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = location.country,
                        fontSize = 18.sp,
                        color = Color(0xFF333333)
                    )
                }
            }
        }
    }
}