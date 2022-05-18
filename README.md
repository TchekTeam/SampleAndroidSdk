# Table of Contents

**[Installation](https://github.com/sofianetchek/sample_android_sdk/blob/main/README.md#installation)**<br><br>
**[Prerequisites](https://github.com/sofianetchek/sample_android_sdk/blob/main/README.md#prerequisites)**<br><br>
**[Usage](https://github.com/sofianetchek/sample_android_sdk/blob/main/README.md#usage)**<br><br>
**[Documentation](https://github.com/sofianetchek/sample_android_sdk/blob/main/README.md#complete-documentation)**<br>
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
    implementation(name: "TchekSdk-1.5", ext: "aar")
    // Required dependencies for TchekSdk
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2"
    implementation 'org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2'

    implementation "androidx.activity:activity-ktx:1.4.0"
    implementation "androidx.appcompat:appcompat:1.4.1"
    implementation "androidx.asynclayoutinflater:asynclayoutinflater:1.0.0"
    implementation "androidx.core:core-ktx:1.7.0"
    implementation "androidx.datastore:datastore-preferences:1.0.0"
    implementation "androidx.exifinterface:exifinterface:1.3.3"
    implementation "androidx.fragment:fragment-ktx:1.4.1"
    implementation "androidx.security:security-crypto:1.1.0-alpha03"
    implementation "androidx.viewpager2:viewpager2:1.0.0"
    implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    def lifecycle_version = "2.4.1"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"

    def camerax_version = "1.1.0-rc01"
    implementation "androidx.camera:camera-camera2:$camerax_version"
    implementation "androidx.camera:camera-lifecycle:$camerax_version"
    implementation "androidx.camera:camera-view:$camerax_version"

    def nav_version = "2.4.2"
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    implementation "eu.davidea:flexible-adapter:5.1.0"
    implementation "eu.davidea:flexible-adapter-ui:1.0.0"

    implementation "com.tbuonomo:dotsindicator:4.2"

    implementation 'io.insert-koin:koin-android:3.1.6'

    implementation "com.google.android.material:material:1.6.0"

    implementation "io.coil-kt:coil:1.4.0"

    def okhttp_version = "4.9.3"
    implementation "com.squareup.okhttp3:okhttp:$okhttp_version"
    // Required dependencies for TchekSdk
}
```

# Usage

*Important: The key used in the documentation and the sample is very limited key, you must use yours to fully use the TchekSDK.*

In order to use the TchekSdk, you must first call the `configure()` method as follows

```kotlin
val builder = TchekBuilder(userId = "SAMPLE_USER_ID") { builder ->
    builder.alertButtonText = android.R.color.holo_orange_dark
    builder.accentColor = android.R.color.holo_orange_light
}

TchekSdk.configure(
    context = this,
    key = "6d52f1de4ffda05cb91c7468e5d99714f5bf3b267b2ae9cca8101d7897d2",
    builder = builder
)
```

# Launch a Shoot Inspect

OR

# Launch Shoot Inspect at End (useful to launch detection)

```kotlin
val builder = TchekShootInspectBuilder(delegate = this, retryCount = 3) { builder ->
    builder.thumbBg = R.color.holo_orange_dark
    builder.thumbCorner = 20f
    builder.thumbDot = R.color.holo_orange_light
    builder.thumbBorder = R.color.holo_orange_dark
    builder.thumbBorderThickness = 16f

    builder.thumbBorderBadImage = R.color.holo_purple
    builder.thumbBorderGoodImage = R.color.holo_orange_light

    builder.btnTuto = R.color.holo_green_dark
    builder.btnTutoText = R.color.black

    builder.btnRetake = R.color.black
    builder.btnRetakeText = R.color.white
    builder.previewBg = R.color.holo_red_dark

    builder.btnEndNext = R.color.holo_purple
    builder.btnEndNextText = R.color.black

    builder.endBg = R.color.holo_blue_light
    builder.endNavBarText = R.color.holo_red_light
    builder.endText = R.color.black

    builder.tutoPageIndicatorDot = R.color.holo_orange_dark
    builder.tutoPageIndicatorDotSelected = R.color.holo_red_dark

    builder.carOverlayGuide = R.color.holo_orange_dark
}

// Launch shoot inspect
TchekSdk.shootInspect(activityContext = this, builder = builder)
// or
// Launch shoot inspect end to go directly to detection step for an existing tchek scan
TchekSdk.shootInspectEnd(activityContext = this, tchekScanId = tchekScanId, builder = builder)

startActivity(intent)
```

[Callback](http://doc.tchek.fr/TchekShootInspectDelegate)
```kotlin
override fun onDetectionEnd(tchekScanId: String, immatriculation: String?) {
}
```

# Launch a Fast Track

```kotlin
val builder = TchekFastTrackBuilder(tchekScanId = tchekScanId, delegate = this) { builder ->
    builder.navBarBg = R.color.holo_blue_light
    builder.navBarText = R.color.holo_red_dark

    builder.fastTrackBg = R.color.holo_purple
    builder.fastTrackText = R.color.holo_orange_dark

    builder.cardBg = R.color.holo_blue_light
    builder.pageIndicatorDot = R.color.holo_orange_dark
    builder.pageIndicatorDotSelected = R.color.holo_red_dark

    builder.damageType = R.color.holo_orange_dark
    builder.damageTypeText = R.color.white
    builder.damageLocation = R.color.holo_blue_light
    builder.damageLocationText = R.color.white
    builder.damageDate = R.color.white
    builder.damageDateText = R.color.darker_gray
    builder.damageNew = R.color.holo_green_dark
    builder.damageNewText = R.color.holo_green_light
    builder.damageOld = R.color.holo_red_dark
    builder.damageOldText = R.color.holo_red_light

    builder.damageCellBorder = R.color.holo_blue_light
    builder.damageCellText = R.color.holo_purple
    builder.damagesListBg = R.color.holo_blue_dark
    builder.damagesListText = R.color.holo_green_light

    builder.btnAddExtraDamage = R.color.holo_orange_dark
    builder.btnAddExtraDamageText = R.color.holo_purple

    builder.btnCreateReport = R.color.holo_green_dark
    builder.btnCreateReportText = R.color.black

    builder.btnValidateExtraDamage = R.color.black
    builder.btnValidateExtraDamageText = R.color.white

    builder.btnDeleteExtraDamage = R.color.holo_purple
    builder.btnDeleteExtraDamageText = R.color.white

    builder.btnEditDamage = R.color.holo_blue_dark
    builder.btnEditDamageText = R.color.darker_gray

    builder.vehiclePatternDamageFill = R.color.holo_orange_dark
    builder.vehiclePatternDamageStroke = R.color.holo_red_dark
    builder.vehiclePatternStroke = R.color.holo_green_light
}

val intent = TchekSdk.fastTrack(activityContext = this, builder = builder)

startActivity(intent)
```
[Callback](http://doc.tchek.fr/TchekFastTrackBuilderDelegate)
```kotlin
override fun onFastTrackEnd(tchekScan: TchekScan) {
}
```

# Display a Report

```kotlin
val builder = TchekReportBuilder(tchekScanId = tchekScanId, delegate = this) { builder ->
    builder.navBarBg = R.color.holo_blue_light
    builder.navBarText = R.color.holo_red_dark

    builder.bg = R.color.holo_purple

    builder.btnDeleteExtraDamage = R.color.white
    builder.btnDeleteExtraDamageText = R.color.holo_purple

    builder.btnNext = R.color.holo_orange_light
    builder.btnNextText = R.color.black

    builder.btnPrev = R.color.holo_red_dark
    builder.btnPrevText = R.color.white

    builder.btnValidateExtraDamage = R.color.holo_blue_dark
    builder.btnValidateExtraDamageText = R.color.holo_orange_light

    builder.btnDeleteExtraDamage = R.color.holo_purple
    builder.btnDeleteExtraDamageText = R.color.white

    builder.btnEditDamage = R.color.holo_blue_dark
    builder.btnEditDamageText = R.color.darker_gray

    builder.btnValidateSignature = R.color.holo_green_light
    builder.btnValidateSignatureText = R.color.darker_gray

    builder.damageCellBorder = R.color.black
    builder.damageCellText = R.color.holo_blue_dark

    builder.extraDamageBg = R.color.holo_orange_dark

    builder.pagingBg = R.color.holo_green_dark
    builder.pagingIndicator = R.color.black
    builder.pagingText = R.color.black
    builder.pagingTextSelected = R.color.holo_red_dark

    builder.repairCostCellCircleDamageCountBg = R.color.holo_blue_bright
    builder.repairCostCellCircleDamageCountText = R.color.white
    builder.repairCostCellCostBg = R.color.holo_orange_dark
    builder.repairCostCellCostText = R.color.holo_blue_light
    builder.repairCostCellText = R.color.holo_red_dark
    builder.repairCostBtnCostSettingsText = R.color.white
    builder.repairCostBtnCostSettings = R.color.holo_red_dark
    builder.repairCostSettingsText = R.color.holo_red_dark
    builder.btnValidateRepairCostEdit = R.color.holo_blue_dark
    builder.btnValidateRepairCostEditText = R.color.holo_orange_light

    builder.reportText = R.color.holo_orange_dark

    builder.signatureBg = R.color.holo_blue_bright

    builder.textFieldBorder = R.color.holo_green_light
    builder.textFieldPlaceHolderText = R.color.darker_gray
    builder.textFieldText = R.color.black

    builder.vehiclePatternDamageFill = R.color.holo_orange_dark
    builder.vehiclePatternDamageStroke = R.color.holo_red_dark
    builder.vehiclePatternStroke = R.color.white

    builder.newDamageBtnDateBorder = R.color.holo_green_light
    builder.newDamageSectionText = R.color.holo_orange_dark
    builder.newDamageCellText = R.color.black
    builder.newDamageOldCompareButton = R.color.holo_purple
    builder.newDamageOldCompareButtonText = R.color.white
    builder.newDamageOldCancelButton = R.color.holo_orange_light
    builder.newDamageOldTitle = R.color.holo_blue_dark
    builder.newDamageOldText = R.color.black

    builder.damageType = R.color.holo_orange_dark
    builder.damageTypeText = R.color.white
    builder.damageLocation = R.color.holo_blue_light
    builder.damageLocationText = R.color.white
    builder.damageDate = R.color.white
    builder.damageDateText = R.color.darker_gray
    builder.damageNew = R.color.holo_green_dark
    builder.damageNewText = R.color.holo_green_light
    builder.damageOld = R.color.holo_red_dark
    builder.damageOldText = R.color.holo_red_light
}

val intent = TchekSdk.report(activityContext = this, builder = builder)

startActivity(intent)
```

[Callback](http://doc.tchek.fr/TchekReportBuilderDelegate)
```kotlin
override fun onReportUpdate(tchekScan: TchekScan) {
}
```

# Complete documentation

[Documentation](http://doc.tchek.fr)
