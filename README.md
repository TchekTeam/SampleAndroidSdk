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
    ...

    // Required dependencies for TchekSdk
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1"
    implementation 'org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2'

    implementation "androidx.activity:activity-ktx:1.4.0"
    implementation "androidx.appcompat:appcompat:1.4.1"
    implementation "androidx.asynclayoutinflater:asynclayoutinflater:1.0.0"
    implementation "androidx.core:core-ktx:1.7.0"
    implementation "androidx.datastore:datastore-preferences:1.0.0"
    implementation "androidx.exifinterface:exifinterface:1.3.3"
    implementation "androidx.fragment:fragment-ktx:1.4.0"
    implementation "androidx.security:security-crypto:1.1.0-alpha03"
    implementation "androidx.viewpager2:viewpager2:1.0.0"
    implementation "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    def lifecycle_version = "2.4.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"

    def camerax_version = "1.1.0-beta01"
    implementation "androidx.camera:camera-camera2:$camerax_version"
    implementation "androidx.camera:camera-lifecycle:$camerax_version"
    implementation "androidx.camera:camera-view:$camerax_version"

    def nav_version = "2.4.0"
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    implementation "eu.davidea:flexible-adapter:5.1.0"
    implementation "eu.davidea:flexible-adapter-ui:1.0.0"

    implementation "com.tbuonomo:dotsindicator:4.2"

    implementation 'io.insert-koin:koin-android:3.1.4'

    implementation "com.google.android.material:material:1.5.0"

    implementation "io.coil-kt:coil:1.3.2"

    def okhttp_version = "4.9.3"
    implementation "com.squareup.okhttp3:okhttp:$okhttp_version"

    implementation "org.jmdns:jmdns:3.5.7"
}
```
# Usage

In your AppDelegate you must call the configure method with your SDK Key

```
let builder = TchekBuilder(userId: "your_user_id", ui: { builder in
	if AppDelegate.CUSTOM_UI {
		builder.alertButtonText = .orange
		builder.accentColor = .orange
	}
})
TchekSdk.configure(key: "my-tchek-sdk-key", builder: builder)
```

# Launch a Shoot Inspect

OR

# Launch Shoot Inspect at End (useful to launch detection)

```
let builder = TchekShootInspectBuilder(retryCount: 3, delegate: self) { builder in
	builder.thumbBg = .brown
	builder.thumbBorder = .blue
	builder.thumbBorderBadImage = .orange
	builder.thumbBorderGoodImage = .green
	builder.thumbDot = .cyan
	builder.thumbBorderThickness = 0
	builder.thumbCorner = 0

	builder.btnTuto = .yellow
	builder.btnTutoText = .cyan
	builder.tutoPageIndicatorDot = .darkGray
	builder.tutoPageIndicatorDotSelected = .blue

	builder.carOverlayGuide = .orange

	builder.btnRetake = .yellow
	builder.btnRetakeText = .cyan

	builder.previewBg = .orange

	builder.btnEndNext = .yellow
	builder.btnEndNextText = .cyan
}

let viewController = TchekSdk.shootInspect(builder: builder)
// OR
let viewController = TchekSdk.shootInspectEnd(tchekId: "any-tchek-id", builder: builder)

// Display the Shoot/Inspect UIViewController
navigationController.pushViewController(viewController, animated: true)
```

# Launch a Fast Track

```
let builder = TchekFastTrackBuilder(tchekId: "any-tchek-id", delegate: self) { builder in
	builder.navBarBg = .purple
	builder.navBarText = .red
	builder.fastTrackBg = .lightGray
	builder.fastTrackText = .purple
	builder.fastTrackPhotoAngle = .red
	builder.fastTrackPhotoAngleText = .orange
	builder.cardBg = .purple

	builder.damagesListBg = .purple
	builder.damagesListText = .red
	builder.damageCellText = .white
	builder.damageCellBorder = .red

	builder.vehiclePatternStroke = .white
	builder.vehiclePatternDamageFill = .orange
	builder.vehiclePatternDamageStoke = .red

	builder.btnAddExtraDamage = .red
	builder.btnAddExtraDamageText = .orange
	builder.btnCreateReport = .yellow
	builder.btnCreateReportText = .cyan

	builder.btnValidateExtraDamage = .yellow
	builder.btnValidateExtraDamageText = .cyan
	builder.btnDeleteExtraDamage = .red
	builder.btnDeleteExtraDamageText = .white
	builder.btnEditDamage = .purple
	builder.btnEditDamageText = .white
}

let viewController = TchekSdk.fastTrack(builder: builder)

// Display the FastTrack UIViewController
navigationController.pushViewController(viewController, animated: true)
```

# Display a Report

```
let builder = TchekReportBuilder(tchekId: "any-tchek-id", delegate: self) { builder in
	builder.bg = .purple
	builder.navBarBg = .purple
	builder.navBarText = .white
	builder.reportText = .lightGray

	builder.btnPrev = .lightGray
	builder.btnPrevText = .darkGray
	builder.btnNext = .black
	builder.btnNextText = .white

	builder.pagingBg = .purple
	builder.pagingText = .lightText
	builder.pagingTextSelected = .white
	builder.pagingIndicator = .white

	builder.textFieldPlaceholderText = .black
	builder.textFieldUnderline = .lightGray
	builder.textFieldUnderlineSelected = .black
	builder.textFieldPlaceholderText = .lightGray
	builder.textFieldPlaceholderTextSelected = .black
	builder.textFieldText = .black

	builder.btnValidateSignature = .yellow
	builder.btnValidateSignatureText = .cyan

	builder.damageCellText = .white
	builder.damageCellBorder = .red
	builder.vehiclePatternStroke = .white
	builder.vehiclePatternDamageFill = .orange

	builder.repairCostCellCostBg = .yellow
	builder.repairCostCellCostText = .cyan
	builder.repairCostCellText = .red
	builder.repairCostCellCircleDamageCountBg = .cyan
	builder.repairCostCellCircleDamageCountText = .white
	builder.repairCostBtnCostSettingsText = .white
	builder.repairCostBtnCostSettings = .red
	builder.repairCostSettingsText = . red
	builder.btnValidateRepairCostEdit = .blue
	builder.btnValidateRepairCostEditText = .orange

	builder.vehiclePatternStroke = .blue
	builder.vehiclePatternDamageFill = .orange
	builder.vehiclePatternDamageStoke = .red

	builder.extraDamageBg = .purple
	builder.btnValidateExtraDamage = .yellow
	builder.btnValidateExtraDamageText = .cyan
	builder.btnDeleteExtraDamage = .red
	builder.btnDeleteExtraDamageText = .white
	builder.btnEditDamage = .purple
	builder.btnEditDamageText = .white
}

let viewController = TchekSdk.report(builder: builder)

// Display the report UIViewController
navigationController.pushViewController(viewController, animated: true)
```

# Delete a Tchek

```
TchekSdk.deleteTchek(tchekId: "any-tchek-id") {
	print("Delete Tchek Failed")
} onSuccess: {
	print("Delete Tchek Success")
}
```

# Complete documentation

[Documentation](http://doc.tchek.fr)
