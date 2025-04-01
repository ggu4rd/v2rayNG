# Minimizing v2rayNG for a React Native Xray Toggle Module

## Keep (Essential Components for Xray Core & Service)

### Xray Core Library Integration

- Include the full Xray core (e.g. the `libv2ray`/Xray `.aar` or native binaries).
- Preserve code that loads and initializes this library (e.g. `Libv2ray.initV2Env(...)`).
- Start/stop the core via `RunLoop/StopLoop` (or equivalent).
- Ensure JNI bindings and any native libraries like `tun2socks` remain.

### VPN Service and Core Control

Keep the Android services that control the Xray lifecycle:

- **V2RayVpnService**: Subclass of `VpnService` to establish VPN tunnel and protect sockets.
- **V2RayServiceManager**: Handles start/stop/configuration.
- **ProcessService**: Optional separate process host for stability.
- **V2RayProxyOnlyService**: Optional service for local proxy (can remove if VPN-only).

### Notification & Foreground Service

- Required to keep the service running and show the VPN key icon.
- Includes `NotificationService` and `startForeground(...)` calls.

### Configuration Loading

- Use a static or dynamic JSON config.
- You may bypass profile management and feed raw config directly to the core.
- Optionally keep a trimmed `V2rayConfigManager` or `AngConfigManager`.

### Asset Files for Xray

- **GeoIP and GeoSite data** (`geoip.dat`, `geosite.dat`) must be included.
- **tun2socks**: Keep native support if not handled directly by Xray.

### Essential Utilities

- Keep `MainApplication` only if needed for core init.
- Keep minimal helper functions from `Utils.kt`.
- Preserve AndroidX/AppCompat dependencies required by `VpnService`.

---

## Remove (Non-Essential UI & Feature Code)

### All UI Activities and Fragments

- Delete `com.vpn1.app.ui`.
- Remove: `MainActivity`, `SettingsActivity`, `ServerActivity`, `LogcatActivity`, etc.

### Profile Management System

- Remove: `ProfileItem`, `SubscriptionItem`, `SubscriptionUpdater`, etc.
- Delete multi-profile logic and subscription updating.
- Drop `V2RayConfigManager` / `AngConfigManager` if unused.

### Settings and Preferences

- Remove `SettingsActivity`, `SettingsViewModel`, and preference XML files.
- Drop MMKV if it's only used for preferences or profiles.
- Delete `SettingsManager`, `MmkvManager`.

### Receivers and Integration Hooks

- Remove:
  - `BootReceiver`
  - `TaskerReceiver`
  - `WidgetProvider`
  - `QSTileService`

### Plugin System

- Remove `com.vpn1.app.plugin` if your config doesn't use external plugins.

### Utilities Not Needed

Remove:

- `QRCodeDecoder`, ZXing-related code
- `HttpUtil`, `PluginUtil`, `SpeedtestManager`, `AppManagerUtil`

### Resource Cleanup

- Delete unused:
  - Layouts, menus, drawables
  - UI strings
  - Preferences XML
- Clean AndroidManifest:
  - Remove `<activity>`, `<receiver>` for deleted components
  - Keep only `<service>` entries and required permissions

### Dependency Pruning

- Remove:
  - Material UI, RecyclerView, Design libraries
  - ZXing, image libraries
  - Retrofit/OkHttp
  - MMKV if unused
  - Analytics/crash-reporting libs

---

## Considerations and Next Steps

### React Native Module Interface

- Expose `start` and `stop` methods to control VPN service.
- Use `context.startService(...)` and `stopService(...)`.

### VPN Permission

- First run needs VPN permission dialog.
- Consider using a minimal Activity or RN bridge to launch `VpnService.prepare()`.

### Config Management

- Recommended: pass JSON config from RN and save to a known file.
- Alternate: build config natively (less ideal).

### Testing and Stability

- Ensure start/stop works cleanly.
- Handle edge cases (start twice, stop when inactive).
- Optionally retain `ServiceControl` logic for state tracking.

### Maintain Required Android Components

- Ensure the Manifest includes:
  ```xml
  <service android:name=".service.V2RayVpnService"
           android:permission="android.permission.BIND_VPN_SERVICE">
    <intent-filter>
      <action android:name="android.net.VpnService" />
    </intent-filter>
  </service>
  ```

## Permissions

- `INTERNET`
- `FOREGROUND_SERVICE`
- `BIND_VPN_SERVICE` (on service only)

---

## Cross-Platform Consideration

- iOS not supported — nothing to do.

---

## Future Updates

- Drop in new `libv2ray.aar` or binaries as needed.
- Monitor `initV2Env` signature/API changes.

---

## Security and Privacy

- React Native module should pass config securely.
- Avoid storing sensitive keys unless encrypted or ephemeral.

---

## Testing on Device

- Confirm VPN tunnel works.
- Use `Log.i` or core logs for debugging (no in-app log viewer).
- Check for tunnel IP/routing.
