package ai.tchek.tcheksdksample

import ai.tchek.tcheksdk.sdk.TchekBuilder
import ai.tchek.tcheksdk.sdk.TchekSdk
import android.app.Application

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val builder = TchekBuilder(userId = "SAMPLE_USER_ID") { builder ->
            builder.alertButtonText = android.R.color.holo_orange_dark
            builder.accentColor = android.R.color.holo_orange_light
        }

        TchekSdk.configure(
            context = this,
            key = "6d52f1de4ffda05cb91c7468e5d99714f5bf3b267b2ae9cca8101d7897d2",
            builder = builder
        )
    }
}