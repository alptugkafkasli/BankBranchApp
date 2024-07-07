import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.alptugkafkasli.bankapp.BranchClickListener
import com.alptugkafkasli.bankapp.BranchItem
import com.alptugkafkasli.bankapp.databinding.RowBranchBinding

class BranchAdapter(
    private var context: Context,
    private var branchList: List<BranchItem>,
    private val listener: BranchClickListener
) : RecyclerView.Adapter<BranchAdapter.ViewHolder>(), Filterable {

    private var filteredBranchList: List<BranchItem> = branchList

    private val colors = arrayOf(
        Color.parseColor("#FFEBEE"), // Light Red
        Color.parseColor("#E3F2FD"), // Light Blue
        Color.parseColor("#E8F5E9"), // Light Green
        Color.parseColor("#FFF3E0"), // Light Orange
        Color.parseColor("#F3E5F5")  // Light Purple
    )

    inner class ViewHolder(val binding: RowBranchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowBranchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val branchItem = filteredBranchList[position]
        with(holder) {
            with(branchItem) {
                binding.SEHIR.text = dc_SEHIR.toString()
                binding.ADRES.text = dc_ADRES.toString()
                binding.ADRESADI.text = dc_ADRES_ADI.toString()
                binding.BANKASUBE.text = dc_BANKA_SUBE.toString()

                binding.BANKATIPI.text = dc_BANKA_TIPI.toString()

            }
            binding.root.setOnClickListener {
                listener.onItemClicked(branchItem)
            }
            binding.root.setBackgroundColor(colors[position % colors.size])
        }
    }

    override fun getItemCount(): Int {
        return filteredBranchList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredBranchList = if (charString.isEmpty()) {
                    branchList
                } else {
                    branchList.filter {
                        it.dc_SEHIR.contains(charString, true)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredBranchList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredBranchList = if (results?.values == null) {
                    emptyList()
                } else {
                    results.values as List<BranchItem>
                }
                notifyDataSetChanged()
            }
        }
    }
}
