# Folder Overview: `com/v2ray/ang`

## 📁 config

Handles loading, saving, parsing, and managing V2Ray/Xray configuration files.

Includes:

- Config data classes
- Conversion helpers
- Optional subscription parsing

**Keep:** Only if you need to parse/modify configs natively.  
**Remove:** If you'll send JSON config from React Native directly.

---

## 📁 plugin

Handles optional plugins like obfuscation tools (e.g. Shadowsocks plugins).

**Keep:** Only if your config uses external plugin binaries.  
**Remove:** In most cases — not needed if you're just running plain Xray.

---

## 📁 receiver

Contains Android `BroadcastReceiver`s:

- Auto-start on boot
- Tasker automation
- Home screen widgets
- Quick Settings tile

**Remove:** All of this unless you want auto-start or Tasker integration.

---

## 📁 service

Contains **core VPN logic**, including:

- `V2RayVpnService`: Android `VpnService` subclass
- `V2RayServiceManager`: Handles start/stop/config
- Foreground service handling
- Logging service

**KEEP THIS FOLDER** — it's essential to starting/stopping the VPN and managing Xray.

---

## 📁 ui

All UI code: Activities, Fragments, Adapters, ViewModels, etc.

**Remove:** All of it if you're replacing the UI with React Native.

---

## 📁 util

Utility classes, e.g.:

- File I/O
- Notification helpers
- Network checks
- Clipboard

**Keep selectively:** Only what's used by the service code. You can trim most.

---

## 📁 Root Package (`com/v2ray/ang`)

Contains:

- `MainApplication.kt`: App-level init (MMKV, etc)
- `ServiceControl.kt`: Tracks if service is running
- `AppConfig.kt`: Constants, package names, etc

**Keep:** `ServiceControl.kt` and `AppConfig.kt` if used in `service/`  
**Trim:** `MainApplication.kt` if it initializes UI-related things or MMKV (not needed)
