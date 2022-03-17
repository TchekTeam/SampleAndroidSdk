package ai.tchek.tcheksdksample

import ai.tchek.tcheksdk.business.companion.TchekCompanionManager
import ai.tchek.tcheksdk.sdk.TchekCompanionBuilder
import ai.tchek.tcheksdk.sdk.TchekCompanionDelegate
import ai.tchek.tcheksdk.sdk.TchekSdk
import ai.tchek.tcheksdksample.databinding.ActivityCompanionBinding
import android.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class CompanionActivity : AppCompatActivity(), TchekCompanionDelegate {
    companion object {
        private const val TAG = "CompanionActivity"
    }

    private var companionManager: TchekCompanionManager? = null
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var viewBinding: ActivityCompanionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCompanionBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        with(viewBinding) {
            startDiscoveryButton.setOnClickListener {
                companionManager?.startDiscovery()
            }

            stopDiscoveryButton.setOnClickListener {
                companionManager?.stopDiscovery()
            }

            showCompanionButton.setOnClickListener {
                companionManager?.showCompanionSelectionScreen(supportFragmentManager)
            }

            detachCompanionButton.setOnClickListener {
                companionManager?.detachCompanion()
            }
        }

        companion()
    }

    private fun companion() {
        val builder = TchekCompanionBuilder(delegate = this) { builder ->
            builder.bg = R.color.holo_orange_dark
            builder.title = R.color.holo_red_dark
            builder.cardBg = R.color.holo_blue_dark
            builder.cardText = R.color.white
            builder.cancelButton = R.color.holo_purple
            builder.cancelButtonText = R.color.black
            builder.connectButton = R.color.holo_green_light
            builder.connectButtonText = R.color.white
        }

        TchekSdk.companionManager(
            builder = builder,
            onFailure = { Log.d(TAG, "TchekSdk.companionManager: onFailure($it)") },
            onReady = ::onCompanionManagerAvailable
        )
    }

    private fun onCompanionManagerAvailable(companionManager: TchekCompanionManager) {
        this.companionManager = companionManager
    }

    override fun onCompanionStateChanged(newState: TchekCompanionManager.CompanionState) {
        Log.d(TAG, "onCompanionStateChanged() called with: newState = $newState")
        updateUI(newState)
    }

    private fun updateUI(state: TchekCompanionManager.CompanionState) {
        handler.post {
            with(viewBinding) {
                companionState.text = "State= ${state.serviceState.name}"
                attachedCompanion.text = "Attached service= ${state.attachedService?.name}"
                availableServices.text = "Available services= ${state.availableServices.map { it.name }}"
            }
        }
    }
}
