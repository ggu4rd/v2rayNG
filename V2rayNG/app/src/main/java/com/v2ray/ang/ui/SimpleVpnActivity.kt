package com.v2ray.ang.ui

import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.v2ray.ang.R
import com.v2ray.ang.service.V2RayServiceManager

class SimpleVpnActivity : AppCompatActivity() {

    private val requestVpnPermission =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // VPN permission granted, start the service
                    V2RayServiceManager.startVServiceFromToggle(this)
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_vpn)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            // Check if VPN permission is needed
            val intent = VpnService.prepare(this)
            if (intent == null) {
                // Permission already granted
                V2RayServiceManager.startVServiceFromToggle(this)
            } else {
                // Request VPN permission
                requestVpnPermission.launch(intent)
            }
        }

        findViewById<Button>(R.id.btnStop).setOnClickListener {
            V2RayServiceManager.stopVService(this)
        }
    }
}
