package ai.tchek.tcheksdksample

import ai.tchek.tcheksdk.domain.TchekScan
import ai.tchek.tcheksdk.sdk.*
import ai.tchek.tcheksdksample.databinding.ActivityMainBinding
import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), TchekShootInspectDelegate, TchekFastTrackDelegate, TchekReportDelegate {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val dynamicTchekScans = mutableListOf<SampleTchekScan>()

    private val preferences by lazy { this.dataStore }
    private val coroutineContext = Dispatchers.Main + SupervisorJob()
    private val handler = Handler(Looper.getMainLooper())
    private val currentScans = mutableListOf<SampleTchekScan>()

    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val adapter = MainAdapter(
        onItemShootInspect = this@MainActivity::shootInspect,
        onItemFastTrack = this@MainActivity::fastTrack,
        onItemReport = this@MainActivity::report,
        data = emptyList()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.btnInit.setOnClickListener {
            actionConfigure()
        }
        viewBinding.btnShootInspect.setOnClickListener { shootInspect() }
        viewBinding.btnReportUrl.setOnClickListener { getReportUrl() }
        viewBinding.scansRecyclerView.adapter = adapter

        CoroutineScope(coroutineContext).launch {
            val savedScans = preferences.previousScansFlow.first()

            dynamicTchekScans.addAll(savedScans)

            currentScans.addAll(dynamicTchekScans)
            currentScans.sortByDescending { it.timestamp }

            updateAdapterAndPreferences()
        }

        configure(false)

        viewBinding.txtFieldSSO.setText("STCHEK")
        viewBinding.txtFieldTchekId.setText("oy1g8JVThA")
    }

    private fun getReportUrl() {
        val tchekId = viewBinding.txtFieldTchekId.text?.toString() ?: ""
        TchekSdk.getReportUrl(tchekId = tchekId,
            validity = 1,
            cost = false,
            onFailure = { error ->
                Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
            },
            onSuccess = { url ->
                MaterialAlertDialogBuilder(this)
                    .setMessage(url)
                    .setCancelable(true)
                    .setNeutralButton("OK", null)
                    .setPositiveButton("Open in Browser") { dialog, _ ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                        dialog.dismiss()
                    }
                    .show()
            })
    }

    private fun updateAdapterAndPreferences() {
        //Fire and forget
        CoroutineScope(coroutineContext).launch {
            preferences.saveSampleScans(dynamicTchekScans)
        }

        currentScans.apply {
            clear()
            addAll(dynamicTchekScans)
        }

        adapter.updateData(currentScans)
    }

    private fun configure(show: Boolean) {
        with(viewBinding) {
            if (show) {
                layoutBtn.isVisible = true
                scansRecyclerView.isVisible = true
            } else {
                layoutBtn.isVisible = false
                scansRecyclerView.isVisible = false
            }
        }
    }

    private fun actionConfigure() {
        configure(false)
        if (viewBinding.switchSSO.isChecked) {
            TchekSdk.configure(
                context = this,
                keySSO = viewBinding.txtFieldSSO.text?.toString() ?: "",
                onCompletion = { tchekId ->
                    Log.d(TAG, "configure-tchekId: $tchekId")
                    configure(true)
                },
                TchekBuilder { builder ->
                    builder.alertButtonText = R.color.holo_orange_dark
                    builder.accentColor = R.color.holo_orange_light
                })
        } else {
            TchekSdk.configure(
                context = this,
                key = "6d52f1de4ffda05cb91c7468e5d99714f5bf3b267b2ae9cca8101d7897d2",
                TchekBuilder(userId = "USER_ID") { builder ->
                    builder.alertButtonText = R.color.holo_orange_dark
                    builder.accentColor = R.color.holo_orange_light
                })
            configure(true)
        }
    }

    private fun shootInspect(tchekScanId: String? = null) {
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

        val intent = if (tchekScanId != null) {
            TchekSdk.shootInspectEnd(activityContext = this, tchekScanId = tchekScanId, builder = builder)
        } else {
            TchekSdk.shootInspect(activityContext = this, builder = builder)
        }

        startActivity(intent)
    }

    private fun fastTrack(tchekScanId: String) {
        val intent = TchekSdk.fastTrack(activityContext = this, TchekFastTrackBuilder(tchekScanId = tchekScanId, delegate = this) { builder ->
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
        })

        startActivity(intent)
    }

    private fun report(tchekScanId: String) {
        val intent = TchekSdk.report(activityContext = this, TchekReportBuilder(tchekScanId = tchekScanId, delegate = this) { builder ->
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

            // Unused
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
        })

        startActivity(intent)
    }

    // region delegate TchekShootInspectDelegate
    override fun onDetectionEnd(tchekScanId: String, immatriculation: String?) {
        Log.d(TAG, "onDetectionEnd() called with: tchekScanId = $tchekScanId, immatriculation = $immatriculation")
        dynamicTchekScans.add(0, SampleTchekScan(tchekScanId, "By Me", System.currentTimeMillis()))
        handler.post {
            updateAdapterAndPreferences()
        }
    }
    // endregion

    // region delegate TchekFastTrackDelegate
    override fun onFastTrackEnd(tchekScan: TchekScan) {
        Log.d(TAG, "onFastTrackEnd() called with: tchekScan = $tchekScan")
    }
    // endregion

    // region delegate TchekReportDelegate
    override fun onReportUpdate(tchekScan: TchekScan) {
        Log.d(TAG, "onReportUpdate() called with: tchekScan = $tchekScan")
    }
    // endregion
}
