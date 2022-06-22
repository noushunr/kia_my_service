package com.serviceapp.kia.ui.fragments.my.booking

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serviceapp.kia.data.network.responses.MyBookingsApi.*
import com.serviceapp.kia.data.network.responses.MyVehiclesApi.*
import com.serviceapp.kia.data.network.responses.VehicleHistoryApi.*
import com.serviceapp.kia.databinding.ListMyBookingsBinding
import com.serviceapp.kia.utils.setMonthDateYear
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/*
 *Created by Adithya T Raj on 04-07-2021
*/

class MyBookingAdapter(): RecyclerView.Adapter<MyBookingAdapter.ViewHolder>() {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    private val diffCallBack = object : DiffUtil.ItemCallback<MyBookingsDataResponse>() {
        override fun areItemsTheSame(
            oldItem: MyBookingsDataResponse,
            newItem: MyBookingsDataResponse
        ): Boolean {
            return oldItem.appoinments_booking_no == newItem.appoinments_booking_no
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: MyBookingsDataResponse,
            newItem: MyBookingsDataResponse
        ): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallBack)

    fun submitList(list: List<MyBookingsDataResponse>) {
        adapterScope.launch {
            withContext(Dispatchers.Main) {
                differ.submitList(list.toList())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    class ViewHolder(
        private val binding: ListMyBookingsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MyBookingsDataResponse) {
            binding.listMyBookingServiceTxt.text = item.servicetype_name
            binding.listMyBookingDateTxt.text = setMonthDateYear(item.appoinments_date.toString())
            binding.listMyBookingSlotTxt.text = item.appoinments_slote
            binding.listMyBookingCenterTxt.text = item.servicecenter_location_address
            binding?.listMyMapClick.setOnClickListener {
                val url =
                    "https://www.google.com/maps/search/?api=1&query=${item.servicecenter_location_address}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                binding?.listMyMapClick.context.startActivity(intent)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListMyBookingsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }


}