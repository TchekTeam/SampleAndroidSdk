package ai.tchek.tcheksdksample

import kotlinx.serialization.Serializable

@Serializable
data class SampleTchekScan(
    val tchekScanId: String,
    val label: String,
    val timestamp: Long
)
