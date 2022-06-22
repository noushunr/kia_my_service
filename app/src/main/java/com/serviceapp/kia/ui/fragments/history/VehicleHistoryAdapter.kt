package com.serviceapp.kia.ui.fragments.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serviceapp.kia.R
import com.serviceapp.kia.data.network.responses.VehicleHistoryApi.*
import com.serviceapp.kia.databinding.ListVehicleHistoryBinding
import com.serviceapp.kia.utils.getFormattedDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt


/*
 *Created by Adithya T Raj on 02-07-2021
*/

class VehicleHistoryAdapter(): RecyclerView.Adapter<VehicleHistoryAdapter.ViewHolder>() {

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    private var hasInitParentDimensions = false
    private var maxLayoutWidth: Int = 0

    private val diffCallBack = object : DiffUtil.ItemCallback<VehicleHistoryDataResponse>() {
        override fun areItemsTheSame(
            oldItem: VehicleHistoryDataResponse,
            newItem: VehicleHistoryDataResponse
        ): Boolean {
            return oldItem.orderno == newItem.orderno
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: VehicleHistoryDataResponse,
            newItem: VehicleHistoryDataResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    fun submitList(list: List<VehicleHistoryDataResponse>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                differ.submitList(list.toList())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        if (!hasInitParentDimensions) {
            maxLayoutWidth = (parent.width * 0.85f).roundToInt()
            hasInitParentDimensions = true
        }
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position], maxLayoutWidth)
    }

    override fun getItemCount() = differ.currentList.size

    class ViewHolder(
        private val binding: ListVehicleHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: VehicleHistoryDataResponse, targetWidth: Int) {
//            binding.root.layoutParams = RecyclerView.LayoutParams(
//                targetWidth,
//                RecyclerView.LayoutParams.MATCH_PARENT
//            )
            var orderNoLabel = binding.listVehicleHistoryOrderIdTxt.context.getString(R.string.order_no)
            binding.listVehicleHistoryOrderIdTxt.text = "$orderNoLabel - ${item.orderno}"
            binding.listVehicleHistoryDateTxt.text = getFormattedDate(item.docdate!!)
            binding.listVehicleHistorySlotTxt.text = item.appoinments_slote
            binding.listVehicleHistoryCenterTxt.text = item.servicecenter_location_address
            binding.listVehicleHistoryPriceTxt.text = "KD " + item.invoicevalue
            binding.listVehicleHistoryDescriptionTxt.text = item.description
            binding.listVehicleHistoryNameTxt.text = item.description
            binding.listVehicleHistoryPlateNoTxt.text = item.regno
            binding.listVehicleHistoryVinNoTxt.text = item.chasis
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListVehicleHistoryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


}