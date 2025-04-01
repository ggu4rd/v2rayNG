package com.vpn1.app.ui

import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.vpn1.app.R
import com.vpn1.app.service.V2RayServiceManager

class MainActivity : AppCompatActivity() {
    private var isVpnRunning = false

    private val requestVpnPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                startVpn()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        findViewById<Button>(R.id.btnToggle).setOnClickListener {
            if (!isVpnRunning) {
                val intent = VpnService.prepare(this)
                if (intent == null) {
                    startVpn()
                } else {
                    requestVpnPermission.launch(intent)
                }
            } else {
                stopVpn()
            }
        }
    }

    private fun startVpn() {
        V2RayServiceManager.startVServiceFromToggle(this)
        isVpnRunning = true
        updateButtonText()
    }

    private fun stopVpn() {
        V2RayServiceManager.stopVService(this)
        isVpnRunning = false
        updateButtonText()
    }

    private fun updateButtonText() {
        findViewById<Button>(R.id.btnToggle).text = 
            getString(if (isVpnRunning) R.string.disconnect_vpn else R.string.connect_vpn)
    }
}