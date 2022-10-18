# Table of Contents

**[Installation](https://github.com/sofianetchek/sample_android_sdk/blob/main/README.md#installation)**<br><br>
**[Prerequisites](https://github.com/sofianetchek/sample_android_sdk/blob/main/README.md#prerequisites)**<br><br>
**[Usage](https://github.com/sofianetchek/sample_android_sdk/blob/main/README.md#usage)**<br><br>
**[UI Style](https://github.com/sofianetchek/sample_android_sdk/blob/main/README.md#ui-style)**<br>
_________________

# Installation

Currently, TchekSdk is distributed as an Android Archive (aar), you must follow these steps to integrate it into your app:

1. Copy / Paste the TchekSDK aar into your project's file tree.

![](https://github.com/sofianetchek/sample_android_sdk/blob/main/Screenshots/Install_1.png?raw=true "")

2. Then, tell gradle to look for this file

2.1. Add the `flatDir { dirs "app/libs" }` to the `settings.gradle` file at the root of your project

![](https://github.com/sofianetchek/sample_android_sdk/blob/main/Screenshots/Install_2.png?raw=true "")

2.2. Add the dependency on the TchekSDK aar to your module's `build.gradle`

![](https://github.com/sofianetchek/sample_android_sdk/blob/main/Screenshots/Install_3.png?raw=true "")

# Prerequisites

Because the TchekSdk is distributed as an aar, you have to manually add most of its dependencies.

*Note: Creating a dedicated maven repository is on the roadmap & will make this step obsolete*

1. Add Jitpack & JCenter repositories to `settings.gradle` (those are required for some dependencies):

![](https://github.com/sofianetchek/sample_android_sdk/blob/main/Screenshots/Install_4.png?raw=true "")

2. Add the following gradle dependencies to your module's `build.gradle` file:

```groovy
dependencies {
    implementation(name: "TchekSdk-1.7", ext: "aar")
    // Required dependencies for TchekSdk
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"
    implementation 'org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2'

    implementation "androidx.activity:activity-ktx:1.6.0"
    implementation "androidx.appcompat:appcompat:1.5.1"
    implementation "androidx.asynclayoutinflater:asynclayoutinflater:1.0.0"
    implementation "androidx.core:core-ktx:1.9.0"
    implementation "androidx.datastore:datastore-preferences:1.0.0"
    implementation "androidx.exifinterface:exifinterface:1.3.3"
    implementation "androidx.fragment:fragment-ktx:1.5.3"
    implementation "androidx.security:security-crypto:1.1.0-alpha03"
    implementation "androidx.viewpager2:viewpager2:1.0.0"
    implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    def lifecycle_version = "2.5.1"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"

    def camerax_version = "1.1.0-rc01"
    implementation "androidx.camera:camera-camera2:$camerax_version"
    implementation "androidx.camera:camera-lifecycle:$camerax_version"
    implementation "androidx.camera:camera-view:$camerax_version"

    def nav_version = "2.5.2"
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    implementation "eu.davidea:flexible-adapter:5.1.0"
    implementation "eu.davidea:flexible-adapter-ui:1.0.0"

    implementation "com.tbuonomo:dotsindicator:4.3"

    implementation 'io.insert-koin:koin-android:3.1.6'

    implementation "com.google.android.material:material:1.6.1"

    implementation "io.coil-kt:coil:1.4.0"
    implementation "io.coil-kt:coil-svg:1.4.0"

    def okhttp_version = "4.10.0"
    implementation "com.squareup.okhttp3:okhttp:$okhttp_version"

    implementation('io.socket:socket.io-client:2.0.0') {
        exclude group: 'org.json', module: 'json'
    }
    // Required dependencies for TchekSdk
}
```

# Usage

*Important: The key used in the documentation and the sample is very limited key, you must use yours to fully use the TchekSDK.*

In order to use the TchekSdk, you must first call the `configure()` method as follows

```kotlin
val builder = TchekBuilder(userId = "USER_ID") { builder ->
    builder.alertButtonText = android.R.color.holo_orange_dark
    builder.accentColor = android.R.color.holo_orange_light
    builder.statusBarColor = android.R.color.holo_orange_dark
}

// with SSO Key
TchekSdk.configure(
    context = this,
    keySSO = viewBinding.txtFieldSSO.text?.toString() ?: "",
    onFailure = { apiError ->
        // TODO
    },
    onSuccess = { tchekSSO ->
        // TODO
    },
    builder = builder
)

// or TchekSDK Key
TchekSdk.configure(
    context = this,
    key = "6d52f1de4ffda05cb91c7468e5d99714f5bf3b267b2ae9cca8101d7897d2",
    onCompletion = {
        // TODO: socket subscriber...
    },
    builder = builder
)
```

# Launch a Shoot Inspect

OR

# Launch Shoot Inspect at End (useful to launch detection)

```kotlin
val builder = TchekShootInspectBuilder(delegate = this) { builder ->
    builder.thumbBg = R.color.holo_orange_dark
    ...
}

// Launch shoot inspect
TchekSdk.shootInspect(activityContext = this, builder = builder)
// or
// Launch shoot inspect end to go directly to detection step for an existing tchek scan
TchekSdk.shootInspectEnd(activityContext = this, tchekScanId = tchekScanId, builder = builder)

startActivity(intent)
```

* Callback

```kotlin
override fun onDetectionInProgress() {
}

override fun onDetectionEnd(tchekScanId: String, immatriculation: String?) {
}
```

# Launch a Fast Track

```kotlin
val builder = TchekFastTrackBuilder(tchekScanId = tchekScanId, delegate = this) { builder ->
    builder.navBarBg = R.color.holo_blue_light
    ...
}

val intent = TchekSdk.fastTrack(activityContext = this, builder = builder)

startActivity(intent)
```

* Callback

```kotlin
override fun onReportCreated(tchekScan: TchekScan) {
}
```

# Display a Report

```kotlin
val builder = TchekReportBuilder(tchekScanId = tchekScanId, delegate = this) { builder ->
    builder.navBarBg = R.color.holo_blue_light
    ...
}

val intent = TchekSdk.report(activityContext = this, builder = builder)

startActivity(intent)
```

* Callback

```kotlin
override fun onReportUpdate(tchekScan: TchekScan) {
}
```

# Load All Tchek

```kotlin
TchekSdk.loadAllTchek(
    type = TchekScanType.Mobile,
    deviceId = null,
    search = null,
    limit = 50,
    page = 0,
    onFailure = { error ->
        Log.d(TAG, "error: $error")
    },
    onSuccess = { tcheks ->
        tcheks.forEach { tchek ->
            Log.d(TAG, "tchek.id: ${tchek.id}, tchek.status: ${tchek.status}")
        }
    }
)
```

![](https://github.com/sofianetchek/sample_android_sdk/blob/main/Screenshots/loadAllTchek.png?raw=true "")

# Socket Subscriber

* Subscribe: eg: on configure completion

```kotlin
tchekSocketManager = TchekSdk.socketManager(TchekScanType.Mobile, null)
tchekSocketManager?.subscribe(newTchekEmitter)
tchekSocketManager?.subscribe(detectionFinishedEmitter)
tchekSocketManager?.subscribe(createReportEmitter)
tchekSocketManager?.subscribe(deleteTchekEmitter)
```

* Do not forget to destroy them

```kotlin
override fun onDestroy() {
    super.onDestroy()
    tchekSocketManager?.destroy()
}
```

* The Emitter

```kotlin
private val newTchekEmitter = object : NewTchekEmitter {
    override fun newTchek(tchek: TchekScan) {
        Log.d(TAG, "newTchekEmitter-NewTchek-tchek.id: ${tchek.id}, tchek.vehicle?.immat: ${tchek.vehicle?.immat}")
    }
}

private val detectionFinishedEmitter = object : DetectionFinishedEmitter {
    override fun detectionFinished(tchek: TchekScan) {
        Log.d(TAG, "detectionFinishedEmitter-detectionFinished-tchek.id: ${tchek.id}, tchek.vehicle?.immat: ${tchek.vehicle?.immat}")
    }
}

private val createReportEmitter = object : CreateReportEmitter {
    override fun createReport(tchek: TchekScan) {
        Log.d(TAG, "createReportEmitter-createReport-tchek.id: ${tchek.id}, tchek.vehicle?.immat: ${tchek.vehicle?.immat}")
    }
}

private val deleteTchekEmitter = object : DeleteTchekEmitter {
    override fun deleteTchek(tchekId: String) {
        Log.d(TAG, "deleteTchekEmitter-deleteTchek-tchekId: $tchekId")
    }
}
```

# UI Style

1. Shoot Inspect

![](https://github.com/sofianetchek/sample_android_sdk/blob/main/Screenshots/SDK_UI_Style-1-ShootInspect.png?raw=true "")

2. Fast Track

![](https://github.com/sofianetchek/sample_android_sdk/blob/main/Screenshots/SDK_UI_Style-2-FastTrack.png?raw=true "")

3. Report

![](https://github.com/sofianetchek/sample_android_sdk/blob/main/Screenshots/SDK_UI_Style-3-Report.png?raw=true "")