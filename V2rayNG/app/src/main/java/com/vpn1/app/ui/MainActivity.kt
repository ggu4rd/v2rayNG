package com.vpn1.app.ui

import android.net.VpnService
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.vpn1.app.R
import com.vpn1.app.service.V2RayServiceManager

class MainActivity : AppCompatActivity() {
    private var isVpnRunning = false
    private lateinit var btnToggle: SwitchCompat
    private lateinit var connectionStatus: TextView

    private val requestVpnPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                startVpn()
            } else {
                // User denied permission, reset switch state
                btnToggle.isChecked = false
                updateConnectionUI()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        btnToggle = findViewById(R.id.btnToggle)
        connectionStatus = findViewById(R.id.connectionStatus)

        // Set initial UI state
        btnToggle.isChecked = isVpnRunning
        updateConnectionUI()

        btnToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && !isVpnRunning) {
                val intent = VpnService.prepare(this)
                if (intent == null) {
                    startVpn()
                } else {
                    requestVpnPermission.launch(intent)
                }
            } else if (!isChecked && isVpnRunning) {
                stopVpn()
            }
        }
    }

    private fun startVpn() {
        V2RayServiceManager.startVServiceFromToggle(this)
        isVpnRunning = true
        btnToggle.isChecked = true
        updateConnectionUI()
    }

    private fun stopVpn() {
        V2RayServiceManager.stopVService(this)
        isVpnRunning = false
        btnToggle.isChecked = false
        updateConnectionUI()
    }

    private fun updateConnectionUI() {
        connectionStatus.text = getString(
            if (isVpnRunning) R.string.connect else R.string.disconnect
        )
    }
}