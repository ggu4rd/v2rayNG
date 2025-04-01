package com.vpn1.app.ui

import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.vpn1.app.R
import com.vpn1.app.service.V2RayServiceManager

class MainActivity : AppCompatActivity() {

    private val requestVpnPermission =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // VPN permission granted, start the service
                    V2RayServiceManager.startVServiceFromToggle(this)
                }
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

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
