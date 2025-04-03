package com.vpn1.app.ui

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
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

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

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

        val serverButton = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.btnServerLocation)
        serverButton.setOnClickListener {
            showServerSelectionModal()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu.findItem(R.id.action_menu)?.setOnMenuItemClickListener {
            showMenuModal()
            true
        }
        return true
    }

    private fun showMenuModal() {
        val builder = AlertDialog.Builder(this, R.style.RoundedDialog)
        val options = arrayOf(getString(R.string.sign_up), getString(R.string.login), getString(R.string.contact_us))

        builder.setItems(options) { _, which ->
            when (which) {
                0 -> handleSignUp()
                1 -> handleLogin()
                2 -> handleContactUs()
            }
        }

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_bg)
        dialog.show()
    }

    private fun handleSignUp() {
        // Add sign up implementation here
    }

    private fun handleLogin() {
        // Add login implementation here
    }

    private fun handleContactUs() {
        val contactUrl = "https://1vpn.org/contact_us"
        val intent = Intent(Intent.ACTION_VIEW, contactUrl.toUri())
        startActivity(intent)
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
            if (isVpnRunning) R.string.connected else R.string.disconnected
        )
    }

    private fun showServerSelectionModal() {
        val builder = AlertDialog.Builder(this, R.style.RoundedDialog)

        // Example server locations - replace with your actual server list
        val locations = arrayOf(
            "Australia",
            "Brazil",
            "Canada",
            "Chile",
            "France",
            "Germany",
            "India",
            "Japan",
            "South Korea",
            "Mexico",
            "Netherlands",
            "Poland",
            "Singapore",
            "South Africa",
            "Spain",
            "Sweden",
            "United Kingdom",
            "USA East",
            "USA South",
            "USA West"
        )

        builder.setItems(locations) { _, which ->
            // Handle server selection
            val selectedLocation = locations[which]
            // TODO: Implement server change logic
        }

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_bg)
        dialog.show()
        dialog.setDialogHeight()

    }

    private fun AlertDialog.setDialogHeight(heightPercent: Int = 90) {
        window?.attributes?.height = (resources.displayMetrics.heightPixels * heightPercent / 100)
        window?.setLayout(window?.attributes?.width ?: 0, window?.attributes?.height ?: 0)
    }
}