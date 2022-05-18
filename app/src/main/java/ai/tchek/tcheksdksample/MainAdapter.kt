package ai.tchek.tcheksdksample

import ai.tchek.tcheksdksample.databinding.ItemScanListBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(
    val onItemShootInspect: (String) -> Unit,
    val onItemFastTrack: (String) -> Unit,
    val onItemReport: (String) -> Unit,
    private var data: List<SampleTchekScan>
) : RecyclerView.Adapter<MainAdapter.Holder>() {

    fun updateData(newData: List<SampleTchekScan>) {
        val notifyOnlyFirst = newData.size - data.size == 1

        data = newData

        if (notifyOnlyFirst) {
            notifyItemInserted(0)
        } else {
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemScanListBinding.inflate(LayoutInflater.from(parent.context))
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = data[position]
        with(holder.viewBinding) {
            name.text = "${item.tchekScanId} - ${item.label}"
            shootInspectButton.setOnClickListener { onItemShootInspect(item.tchekScanId) }
            fastTrackButton.setOnClickListener { onItemFastTrack(item.tchekScanId) }
            reportButton.setOnClickListener { onItemReport(item.tchekScanId) }
        }
    }

    override fun getItemCount(): Int = data.size

    class Holder(val viewBinding: ItemScanListBinding) : RecyclerView.ViewHolder(viewBinding.root)
}