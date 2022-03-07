package ai.tchek.tcheksdksample

import ai.tchek.tcheksdk.domain.TchekScan
import ai.tchek.tcheksdk.sdk.*
import ai.tchek.tcheksdksample.databinding.ActivityMainBinding
import android.R
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), TchekShootInspectDelegate, TchekFastTrackDelegate, TchekReportDelegate {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val preferences by lazy { this.dataStore }
    private val coroutineContext = Dispatchers.Main + SupervisorJob()
    private val handler = Handler(Looper.getMainLooper())
    private val currentScans = mutableListOf<SampleTchekScan>()

    private val adapter = Adapter(
        onItemShootInspect = this@MainActivity::shootInspect,
        onItemFastTrack = this@MainActivity::fastTrack,
        onItemReport = this@MainActivity::report,
        data = emptyList()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.shootInspectButton.setOnClickListener { shootInspect() }
        viewBinding.companionButton.setOnClickListener { showCompanion() }
        viewBinding.scansRecyclerView.adapter = adapter

        CoroutineScope(coroutineContext).launch {
            val savedScans = preferences.previousScansFlow.first()

            currentScans.addAll(savedScans)
            currentScans.sortByDescending { it.timestamp }

            updateAdapterAndPreferences()
        }
    }

    private fun showCompanion() {
        startActivity(Intent(this, CompanionActivity::class.java))
    }

    private fun updateAdapterAndPreferences() {
        val scans = currentScans.toList()

        //Fire and forget
        CoroutineScope(coroutineContext).launch {
            preferences.saveSampleScans(scans)
        }

        adapter.updateData(scans)
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
        val builder = TchekFastTrackBuilder(tchekScanId = tchekScanId, delegate = this) { builder ->
            builder.navBarBg = R.color.holo_blue_light
            builder.navBarText = R.color.holo_red_dark

            builder.fastTrackBg = R.color.holo_purple
            builder.fastTrackText = R.color.holo_orange_dark

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

            builder.cardBg = R.color.holo_blue_light

            builder.damageCellBorder = R.color.holo_blue_light
            builder.damageCellText = R.color.holo_purple
            builder.damagesListBg = R.color.holo_blue_dark
            builder.damagesListText = R.color.holo_green_light

            builder.vehiclePatternDamageFill = R.color.holo_orange_dark
            builder.vehiclePatternDamageStroke = R.color.holo_red_dark
            builder.vehiclePatternStroke = R.color.holo_green_light
        }

        val intent = TchekSdk.fastTrack(activityContext = this, builder = builder)

        startActivity(intent)
    }

    private fun report(tchekScanId: String) {
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

            // Unused
            builder.signatureBg = R.color.holo_blue_bright

            builder.textFieldBorder = R.color.holo_green_light
            builder.textFieldPlaceHolderText = R.color.darker_gray
            builder.textFieldText = R.color.black

            builder.vehiclePatternDamageFill = R.color.holo_orange_dark
            builder.vehiclePatternDamageStroke = R.color.holo_red_dark
            builder.vehiclePatternStroke = R.color.white
        }

        val intent = TchekSdk.report(activityContext = this, builder = builder)

        startActivity(intent)
    }

    override fun onDetectionEnd(tchekScanId: String, immatriculation: String?) {
        Log.d(TAG, "onDetectionEnd() called with: tchekScanId = $tchekScanId, immatriculation = $immatriculation")
        currentScans.add(0, SampleTchekScan(tchekScanId, "By Me", System.currentTimeMillis()))
        handler.post {
            updateAdapterAndPreferences()
        }
    }

    override fun onFastTrackEnd(tchekScan: TchekScan) {
        Log.d(TAG, "onFastTrackEnd() called with: tchekScan = $tchekScan")
    }

    override fun onReportUpdate(tchekScan: TchekScan) {
        Log.d(TAG, "onReportUpdate() called with: tchekScan = $tchekScan")
    }
}
